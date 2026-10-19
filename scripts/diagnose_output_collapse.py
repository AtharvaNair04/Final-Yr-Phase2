import sys
from collections import Counter
from pathlib import Path

import torch
import torch.nn.functional as F

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from model import ModelConfig, ExecutionSemanticTransformer
from vocabulary import Vocabulary
from execution_dataset import ExecutionTraceDataset

checkpoint_path = ROOT / "checkpoints/pilot_mps/checkpoint_best.pt"
device = torch.device("mps" if torch.backends.mps.is_available() else "cpu")

if device.type == "mps":
    torch.backends.mha.set_fastpath_enabled(False)

src_vocab = Vocabulary.load(ROOT / "vocabularies/vocab_source.json")
tgt_vocab = Vocabulary.load(ROOT / "vocabularies/vocab_target.json")

ckpt = torch.load(checkpoint_path, map_location="cpu", weights_only=False)
config = ModelConfig.from_dict(ckpt["model_config"])

model = ExecutionSemanticTransformer(config)
model.load_state_dict(ckpt["model_state_dict"], strict=True)
model.to(device).eval()

dataset = ExecutionTraceDataset(
    jsonl_path=ROOT / "Module3_Training_Dataset/split/test.jsonl",
    java_dir=ROOT / "selected source code",
    source_vocab=src_vocab,
    target_vocab=tgt_vocab,
    max_chunk_size=1024,
    max_source_length=config.max_src_length,
)

item = dataset[33]
src = torch.tensor([item.source_ids], dtype=torch.long, device=device)
dec = torch.tensor([item.decoder_input_ids], dtype=torch.long, device=device)
target = torch.tensor([item.target_ids], dtype=torch.long, device=device)

with torch.inference_mode():
    logits = model(
        src,
        dec,
        src_padding_mask=src.eq(config.src_pad_id),
        target_padding_mask=dec.eq(config.tgt_pad_id),
    )

n = min(logits.shape[1], target.shape[1])
logits = logits[:, :n, :]
target = target[:, :n]

print("=" * 60)
print("1. TOKEN IDS")
print("=" * 60)
for token in (
    "<PAD>", "<UNK>", "<BOS>", "<EOS>",
    "<EVENT_START>", "<EVENT_END>", "<ATTR_START>", "<ATTR_END>",
):
    try:
        ids = tgt_vocab.encode([token])
        print(f"{token:20s} -> {ids}")
    except Exception as exc:
        print(f"{token:20s} -> lookup error: {exc}")

print("\n" + "=" * 60)
print("2. OUTPUT BIAS: HIGHEST VALUES")
print("=" * 60)
bias = model.output_projection.bias.detach().float().cpu()
top_bias = torch.topk(bias, k=15)
for token_id, value in zip(top_bias.indices.tolist(), top_bias.values.tolist()):
    print(f"id={token_id:5d} bias={value: .6f} token={tgt_vocab.decode([token_id])}")

print("\nBias for structural tokens:")
for token in (
    "<PAD>", "<UNK>", "<BOS>", "<EOS>",
    "<EVENT_START>", "<EVENT_END>", "<ATTR_START>", "<ATTR_END>",
):
    token_id = tgt_vocab.encode([token])[0]
    print(f"{token:20s} id={token_id:5d} bias={bias[token_id].item(): .6f}")

print("\n" + "=" * 60)
print("3. LOGIT AND LOSS DIAGNOSTICS")
print("=" * 60)
print("Logit shape:", tuple(logits.shape))
print("Overall logit mean:", logits.float().mean().item())
print("Overall logit std:", logits.float().std().item())
print("Mean maximum logit:", logits.float().max(dim=-1).values.mean().item())

valid = target.ne(config.tgt_pad_id)
loss = F.cross_entropy(
    logits.reshape(-1, logits.shape[-1]).float(),
    target.reshape(-1),
    ignore_index=config.tgt_pad_id,
)
predicted = logits.argmax(dim=-1)
correct = ((predicted == target) & valid).sum().item()
count = valid.sum().item()

print("Cross-entropy loss:", loss.item())
print("Token accuracy:", correct / max(count, 1))
print("Predicted unique token IDs:", predicted.unique().numel())

print("\nFirst 12 positions: reference vs top 3 predictions")
for pos in range(min(12, n)):
    ref_id = int(target[0, pos].item())
    top = torch.topk(logits[0, pos].float(), k=3)
    candidates = [
        (tgt_vocab.decode([int(idx)])[0], round(float(score), 3))
        for idx, score in zip(top.indices.cpu(), top.values.cpu())
    ]
    print(
        f"{pos:2d}: reference={tgt_vocab.decode([ref_id])[0]!r}; "
        f"top3={candidates}"
    )

print("\n" + "=" * 60)
print("4. TARGET TOKEN FREQUENCY IN PILOT TRAINING SUBSET")
print("=" * 60)
train_path = ROOT / "checkpoints/pilot_mps/train_subset.jsonl"
import json

frequency = Counter()
records = 0
with train_path.open() as f:
    for line in f:
        record = json.loads(line)
        tokens = record.get("chunk_tokens", [])
        frequency.update(tokens)
        records += 1

print("Training subset records:", records)
print("Total target tokens:", sum(frequency.values()))
print("Most frequent target tokens:")
for token, count in frequency.most_common(20):
    print(f"{count:8d}  {token!r}")

print("\nMost frequent predicted tokens:")
predicted_tokens = Counter(
    tgt_vocab.decode([int(x)])[0]
    for x in predicted[0].detach().cpu().tolist()
)
for token, count in predicted_tokens.most_common(10):
    print(f"{count:8d}  {token!r}")
