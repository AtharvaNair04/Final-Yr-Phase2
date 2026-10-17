
import torch
from pathlib import Path

checkpoint_dir = Path("checkpoints/exp1")

for name in ["checkpoint_best.pt", "checkpoint_latest.pt"]:
    path = checkpoint_dir / name

    print(f"\n{'=' * 55}")
    print(name)
    print(f"File size: {path.stat().st_size / (1024**2):.2f} MiB")

    # Use only for checkpoint files from your own trusted experiment.
    checkpoint = torch.load(
        path,
        map_location="cpu",
        weights_only=False,
    )

    print("\nCheckpoint keys:")
    print(list(checkpoint.keys()))

    for key in [
        "epoch",
        "val_loss",
        "best_val_loss",
        "model_config",
        "training_config",
    ]:
        if key in checkpoint:
            print(f"\n{key}: {checkpoint[key]}")

    for key in ["model_state_dict", "model_state", "optimizer_state_dict",
                "optimizer_state"]:
        if key in checkpoint:
            value = checkpoint[key]
            if isinstance(value, dict):
                print(f"{key}: present ({len(value)} entries)")
            else:
                print(f"{key}: present ({type(value).__name__})")
