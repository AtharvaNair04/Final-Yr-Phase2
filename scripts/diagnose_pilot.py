import sys
from pathlib import Path

import torch

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

# Match the example selected in the previous inference run.
item = dataset[33]

src = torch.tensor([item.source_ids], dtype=torch.long, device=device)
dec = torch.tensor(
    [item.decoder_input_ids], dtype=torch.long, device=device
)
target = torch.tensor(
    [item.target_ids], dtype=torch.long, device=device
)
src_mask = src.eq(config.src_pad_id)
tgt_mask = dec.eq(config.tgt_pad_id)

with torch.inference_mode():
    logits = model(
        src,
        dec,
        src_padding_mask=src_mask,
        target_padding_mask=tgt_mask,
    )

# Ignore the final position if it exceeds the available target logits.
n = min(logits.shape[1], target.shape[1])
predicted = logits[:, :n, :].argmax(dim=-1)
expected = target[:, :n]

valid = expected.ne(config.tgt_pad_id)
correct = (predicted.eq(expected) & valid).sum().item()
total = valid.sum().item()
accuracy = correct / max(total, 1)

print("===== TEACHER-FORCED DIAGNOSTIC =====")
print("Device:", device)
print("Program:", item.program_id)
print("Chunk:", item.chunk_index + 1, "/", item.total_chunks)
print("Reference target length:", len(item.target_ids))
print("Compared positions:", total)
print(f"Teacher-forced token accuracy: {accuracy:.4f}")

print("\nFirst 50 reference tokens:")
print(" ".join(tgt_vocab.decode(item.target_ids[:50])))

print("\nFirst 50 teacher-forced predictions:")
print(" ".join(tgt_vocab.decode(predicted[0, :50].tolist())))

print("\nMost frequent predicted tokens:")
values, counts = torch.unique(
    predicted[0, :n], return_counts=True
)
order = torch.argsort(counts, descending=True)[:10]
for index in order.tolist():
    token_id = int(values[index].item())
    print(
        repr(tgt_vocab.decode([token_id])[0]),
        int(counts[index].item()),
    )
