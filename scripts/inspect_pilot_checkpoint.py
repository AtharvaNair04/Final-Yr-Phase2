import json
from pathlib import Path

import torch

ROOT = Path(__file__).resolve().parents[1]
checkpoint_path = ROOT / "checkpoints/pilot_mps/checkpoint_best.pt"

ckpt = torch.load(
    checkpoint_path,
    map_location="cpu",
    weights_only=False,
)

print("=" * 65)
print("CHECKPOINT METADATA")
print("=" * 65)

for key in (
    "epoch",
    "global_step",
    "best_val_loss",
    "val_loss",
    "train_loss",
    "model_config",
    "training_config",
):
    print(f"\n{key}:")
    print(json.dumps(ckpt.get(key), indent=2, default=str))

print("\n" + "=" * 65)
print("SAVED MODEL TENSORS")
print("=" * 65)

state = ckpt["model_state_dict"]

for name, tensor in state.items():
    if not torch.is_tensor(tensor):
        continue

    values = tensor.detach().float()
    print(
        f"{name:65s}",
        f"shape={str(tuple(tensor.shape)):25s}",
        f"mean={values.mean().item(): .6f}",
        f"std={values.std().item(): .6f}",
    )

print("\n" + "=" * 65)
print("CHECKPOINT KEYS")
print("=" * 65)
print(sorted(ckpt.keys()))
