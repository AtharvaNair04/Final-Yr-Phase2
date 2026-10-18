# Execution Trace Learning Project --- Complete Status and Roadmap

## 1. Project Overview

This project investigates whether the dynamic execution behaviour of
Java programs can be learned directly from Java source code using a
Transformer-based architecture.

The overall research objective is:

``` text
Java Source Code
      ↓
Learn source semantics
      ↓
Predict runtime execution behaviour
      ↓
Predicted Runtime Event Sequence
```

The proposed model is the **Execution Semantic Transformer (EST)**,
based on a Transformer encoder-decoder architecture.

The work is organized conceptually into:

1.  Data collection
2.  Runtime trace preprocessing
3.  Static program representation
4.  Source-trace dataset construction
5.  Semantic input representation
6.  Execution Semantic Transformer
7.  Training and evaluation

The work completed so far is primarily concentrated on **Module 3** and
the initial analysis/design work for **Module 4**.

------------------------------------------------------------------------

# 2. Overall Methodology

``` text
IBM Project CodeNet
        ↓
Java Programs
        ↓
JIVE Execution
        ↓
Ground-Truth Runtime Traces
        ↓
Source–Trace Pairing
        ↓
Module 3: Structured Runtime Representation
        ↓
Module 4: Tokenization and Numerical Representation
        ↓
Execution Semantic Transformer
        ↓
Predicted Runtime Event Sequence
        ↓
Evaluation
```

The learning direction is:

``` text
Java Source
    ↓
Transformer Encoder
    ↓
Execution-Relevant Context
    ↓
Transformer Decoder
    ↓
Runtime Event Sequence
```

------------------------------------------------------------------------

# 3. Data Source

The project uses Java programs from the **IBM Project CodeNet** dataset.

The original collection contains substantially more Java programs than
the number for which usable JIVE execution traces were generated.

Therefore, the project first had to identify the Java programs that have
corresponding execution traces.

------------------------------------------------------------------------

# 4. Source--Trace Matching

## `extract_matching_sources.py`

### Purpose

This script solves the mismatch between the available Java source files
and the smaller set of programs for which execution traces exist.

The source file and trace use the same program identifier.

Example:

``` text
p00792.java
p00792.csv
```

The script:

1.  Reads the available execution traces.
2.  Extracts their program IDs.
3.  Finds Java files with matching IDs.
4.  Copies only matching Java source files.

### Output

``` text
selected source code/
```

### Why it is necessary

A supervised source-to-runtime example requires both:

``` text
Source Program + Corresponding Runtime Trace
```

A source file without a trace cannot form a complete training pair.

------------------------------------------------------------------------

# 5. Abstract Execution Traces

The original abstract execution traces were available as CSV files.

They contained information corresponding to runtime events such as:

-   Thread Start
-   Method Call
-   New Object
-   Variable Write
-   Field Write
-   Field Read
-   Variable Delete
-   Method Entry
-   Method Exit
-   Thread End

The original event names were verbose, so a compact and normalized event
vocabulary was introduced.

------------------------------------------------------------------------

# 6. Runtime Trace Compaction

## `compact_traces.py`

### Purpose

Converts verbose event names into compact labels while retaining their
semantic information.

Representative mapping:

``` text
Thread Start    → TS
Method Call     → MC
New Object      → NO
Variable Write  → VW
Field Write     → FW
Field Read      → FR
Variable Delete → VD
Method Entry    → ME
Method Exit     → MX
Thread End      → TE
```

The output is stored in:

``` text
compact execution traces/
```

The original filename is preserved.

### Important point

Compaction is **normalization**, not deletion of semantics. Event
attributes and execution order remain available.

------------------------------------------------------------------------

# 7. Structured Runtime Event Representation (SRER)

## `create_esr.py`

This is a central Module 3 component.

The script converts compact runtime traces into a structured event
representation.

The project initially used the name **ESR** in generated JSON, while the
methodology uses **Structured Runtime Event Representation (SRER)**.

For the final report, one terminology should be used consistently. If
the formal term is SRER, the JSON label can be standardized from `"ESR"`
to `"SRER"`.

A representative record is:

``` json
{
  "representation": "ESR",
  "program_id": "p00007",
  "event_count": 23,
  "events": [
    {
      "step": 1,
      "type": "TS",
      "attributes": {
        "VALUE_1": "main (1)"
      }
    }
  ]
}
```

Each event has:

``` text
Event
├── step
├── type
└── attributes
       ├── key
       └── value
```

Example:

``` json
{
  "step": 2,
  "type": "MC",
  "attributes": {
    "CALLER": "SYSTEM",
    "TARGET": "Main#main:1"
  }
}
```

This means:

``` text
Step   = 2
Type   = MC
Caller = SYSTEM
Target = Main#main:1
```

------------------------------------------------------------------------

# 8. SRER Properties

SRER is designed around three important properties.

### Event-centric

Each runtime event is treated as an independent semantic unit.

### Attribute-aware

Attributes remain attached to their corresponding event.

For example:

``` text
MC
 ├── CALLER
 └── TARGET
```

### Order-preserving

The `step` field preserves execution order:

``` text
step 1 → step 2 → step 3 → ...
```

This is essential because runtime execution semantics depend on
ordering.

------------------------------------------------------------------------

# 9. Dynamic Event Schema

The project also defined event-specific schemas.

Examples:

``` text
MC  → CALLER, TARGET
ME  → METHOD, CLASS
MX  → METHOD, RETURN_VALUE
VW  → VARIABLE, VALUE
NO  → CLASS, OBJECT_ID
TS  → THREAD
TE  → THREAD
```

The exact schema should remain consistent with the attributes actually
produced by the JIVE traces.

The purpose is to preserve the relationship between an event type and
its meaningful attributes.

------------------------------------------------------------------------

# 10. Static Program Representation

## `source_parser.py`

The runtime trace describes dynamic behaviour, while Java source code
describes static structure.

This script parses Java source and extracts:

-   Program ID
-   File
-   Package
-   Classes
-   Interfaces
-   Methods
-   Constructors
-   Variables
-   Method calls
-   Control statements

Example:

``` text
Classes:
    Main
    AOJ1212

Methods:
    main
    solve
    intersectionPoint
    sum
    diff
    mul
    cross

Control statements:
    while
    if
    for
```

This is **static information only**.

It does not tell us what happened during execution.

Thus:

``` text
Static Program Representation
            ≠
Runtime Execution Representation
```

They complement each other.

------------------------------------------------------------------------

# 11. Source--Trace Pair Construction

## `create_training_pairs.py`

This script creates the supervised learning examples.

It matches:

``` text
Static Program Representation
          +
Corresponding SRER
```

using the program ID.

The resulting pair represents:

``` text
Input  → Source program representation
Target → Runtime event representation
```

A whole program is paired with its whole runtime event sequence because
one source statement can generate multiple runtime events.

Example:

``` java
machine.insertCoin();
```

may produce:

``` text
MC
↓
ME
↓
VW
↓
MX
```

Therefore, the intended learning unit is:

``` text
Complete Source Program
          ↓
Complete Runtime Event Sequence
```

------------------------------------------------------------------------

# 12. Pair Validation

## `validate_pairs.py`

This script checks whether generated source--trace pairs are usable.

It verifies the presence and consistency of the source and runtime
information and detects empty runtime representations.

The validation produced:

``` text
Total pairs : 563
Valid pairs : 562
Invalid pairs : 1
```

The invalid record was:

``` text
p00295
```

because:

``` text
ESR contains no events
```

The usable dataset therefore contains:

``` text
562 valid source–runtime pairs
```

------------------------------------------------------------------------

# 13. Consolidated Module 3 Dataset

## `create_training_dataset.py`

This script consolidates validated source--trace pairs.

The record structure is approximately:

``` json
{
  "program_id": "p00007",
  "source": {...},
  "runtime_trace": {...}
}
```

The dataset was inspected and confirmed to contain:

``` text
program_id    → string
source        → dictionary
runtime_trace → dictionary
```

This is the main structured dataset produced by Module 3.

------------------------------------------------------------------------

# 14. Dataset Inspection

## `inspect_dataset.py`

This is a verification/debugging utility.

It was used to inspect:

-   top-level keys,
-   data types,
-   nested structure,
-   runtime event structure.

The observed top-level structure was:

``` text
program_id
source
runtime_trace
```

with types:

``` text
program_id    : str
source        : dict
runtime_trace : dict
```

------------------------------------------------------------------------

# 15. Train / Validation / Test Split

## `split_dataset.py`

The 562 valid records were split into:

``` text
Training    = 449
Validation  = 56
Test        = 57
Total       = 562
```

Output:

``` text
split/
├── train.jsonl
├── validation.jsonl
└── test.jsonl
```

The split is performed at the complete program/source-trace-pair level
so that a program's source and trace remain together.

------------------------------------------------------------------------

# 16. Module 3 Output

Module 3 has therefore established:

``` text
Java Source
     +
Runtime Trace
     ↓
Static Representation + SRER
     ↓
Validated Source–Trace Pairs
     ↓
Train / Validation / Test Dataset
```

The central achievement of Module 3 is the creation of a clean
structured dataset that preserves both static program information and
dynamic runtime behaviour.

------------------------------------------------------------------------

# 17. Sequence-Length Analysis

## `analyze_lengths.py`

Before finalizing Module 4, the sequence lengths were analyzed.

The script calculates:

-   Count
-   Minimum
-   Maximum
-   Mean
-   Median
-   95th percentile
-   99th percentile

This prevents selecting an arbitrary Transformer maximum sequence
length.

------------------------------------------------------------------------

# 18. Source Sequence Results

For all 562 records:

``` text
Minimum      = 32
Maximum      = 918
Mean         = 128.85
Median       = 90
95th %ile    = 348.6
99th %ile    = 568.72
```

The source side is comparatively manageable.

------------------------------------------------------------------------

# 19. Runtime Sequence Results

For all 562 records:

``` text
Minimum      = 51
Maximum      = 3,692,475
Mean         = 125,061.67
Median       = 4,963
95th %ile    = 842,655.9
99th %ile    = 2,093,079.37
```

This revealed a major problem:

> Runtime traces have a very long tail and can become extremely large.

A standard Transformer cannot practically process or generate millions
of tokens as a single ordinary sequence.

------------------------------------------------------------------------

# 20. Runtime Event Count Analysis

## `analyze_event_lengths.py`

This analysis counts runtime events rather than only tokens.

Results:

``` text
Programs       = 562
Minimum events = 5
Maximum events = 369,211
Mean           = 11,677.61
Median         = 438
95th %ile      = 79,971.45
99th %ile      = 198,256.50
```

This confirms that runtime traces can be dramatically longer than source
sequences.

------------------------------------------------------------------------

# 21. Event-Aware Runtime Tokenization

## `tokenize_and_analyze.py`

The current Module 4 prototype investigates event-aware tokenization.

The principle is:

> Each runtime event should remain a recognizable semantic unit after
> tokenization.

Example SRER event:

``` json
{
  "step": 2,
  "type": "MC",
  "attributes": {
    "CALLER": "SYSTEM",
    "TARGET": "Main#main:1"
  }
}
```

Possible event-aware token sequence:

``` text
<EVENT_START>
TYPE
MC
STEP
2
CALLER
SYSTEM
TARGET
Main#main:1
<EVENT_END>
```

Another event:

``` json
{
  "step": 4,
  "type": "VW",
  "attributes": {
    "CONTEXT": "Main#main:1",
    "ARGS": "java.lang.String[]:1"
  }
}
```

becomes:

``` text
<EVENT_START>
TYPE
VW
STEP
4
CONTEXT
Main#main:1
ARGS
java.lang.String[]:1
<EVENT_END>
```

The explicit event boundaries preserve:

-   event start,
-   event end,
-   event type,
-   event attributes,
-   event order.

------------------------------------------------------------------------

# 22. Source Tokenization

The source side is tokenized separately.

Example:

``` java
machine.insertCoin();
```

can become:

``` text
machine
.
insertCoin
(
)
;
```

The final source tokenizer should be Java-aware/AST-aware and should be
consistent with the final methodology.

------------------------------------------------------------------------

# 23. Module 4 Intended Workflow

Module 4 converts structured representations into Transformer-ready
numerical representations.

Conceptually:

``` text
Structured Source
       ↓
Source Tokenizer
       ↓
Source Vocabulary
       ↓
Source Token IDs
       ↓
Embeddings
```

Runtime side:

``` text
SRER
 ↓
Event-Aware Runtime Tokenizer
 ↓
Runtime Vocabulary
 ↓
Runtime Token IDs
 ↓
Special Tokens
 ↓
Event-Preserving Chunking
 ↓
Padding
 ↓
Attention Masks
 ↓
Embeddings
```

------------------------------------------------------------------------

# 24. Vocabulary

Two vocabularies are conceptually appropriate because source and runtime
representations use different token spaces.

### Source vocabulary

Examples:

``` text
public
class
Main
insertCoin
```

### Runtime vocabulary

Examples:

``` text
<EVENT_START>
TYPE
MC
CALLER
SYSTEM
TARGET
<EVENT_END>
```

Each token receives an integer ID.

------------------------------------------------------------------------

# 25. Special Tokens

The intended special tokens are:

``` text
<SOS>
<EOS>
<PAD>
<UNK>
```

Their roles:

-   `<SOS>` --- start of decoder generation
-   `<EOS>` --- end of generation
-   `<PAD>` --- batch padding
-   `<UNK>` --- unknown token

------------------------------------------------------------------------

# 26. Padding and Attention Masks

Different programs have different sequence lengths.

Padding makes samples in a batch have compatible dimensions.

Example:

``` text
Tokens: 15 23 18 0 0
Mask:    1  1  1 0 0
```

The mask tells the Transformer that padded positions are not meaningful.

------------------------------------------------------------------------

# 27. Embeddings

Token IDs are converted into dense vectors.

Conceptually:

``` text
MC
 ↓
Token ID
 ↓
Dense embedding vector
```

The vectors are learned during model training.

------------------------------------------------------------------------

# 28. Positional Information

Transformers require positional information to distinguish different
token orders.

For runtime traces:

``` text
MC → ME → VW
```

is different from:

``` text
VW → ME → MC
```

Therefore positional information is essential for preserving execution
order.

------------------------------------------------------------------------

# 29. Execution Semantic Transformer (EST)

The proposed model is:

> **Execution Semantic Transformer (EST)**

Its objective is:

> Generate runtime event sequences conditioned on Java source code.

The proposed architecture is:

``` text
Tokenized Java Source
        ↓
Source Embeddings
        ↓
Positional Encoding
        ↓
Transformer Encoder
        ↓
Execution Semantic Space (ESS)
        ↓
Autoregressive Transformer Decoder
        ↓
Predicted Runtime Event Sequence
```

------------------------------------------------------------------------

# 30. Encoder

The encoder processes tokenized Java source.

It contains:

-   source embeddings,
-   positional information,
-   multi-head self-attention,
-   feed-forward layers,
-   residual connections,
-   normalization,
-   multiple encoder layers.

The encoder learns contextual relationships within the source program.

------------------------------------------------------------------------

# 31. Execution Semantic Space (ESS)

The proposed architecture includes an intermediate:

> **Execution Semantic Space (ESS)**

ESS is intended to represent source information relevant to runtime
execution behaviour.

Conceptually:

``` text
Source Encoder
      ↓
Contextual Source Representation
      ↓
ESS
      ↓
Decoder Cross-Attention
```

ESS is currently a proposed architectural component and still needs a
precise mathematical definition and implementation.

------------------------------------------------------------------------

# 32. Decoder

The decoder generates the runtime event sequence autoregressively.

During training, shifted target tokens are used with teacher forcing.

Example:

``` text
Input:
<SOS> EVENT_START TYPE

Target:
MC
```

The decoder uses:

### Masked self-attention

Prevents access to future target tokens.

### Cross-attention

Allows the decoder to attend to the encoder/ESS representation of the
source program.

Thus:

``` text
Java Source
    ↓
Encoder
    ↓
ESS
    ↓
Cross-Attention
    ↑
Decoder
    ↓
Runtime Event Tokens
```

------------------------------------------------------------------------

# 33. Training

The intended training setup includes:

-   mini-batch training,
-   teacher forcing,
-   cross-entropy loss,
-   backpropagation,
-   Adam optimizer,
-   validation,
-   model checkpointing.

Training flow:

``` text
Source
  ↓
Encoder
  ↓
ESS
  ↓
Decoder
  ↓
Predicted runtime tokens
  ↓
Compare with ground truth
  ↓
Cross-entropy loss
  ↓
Backpropagation
  ↓
Parameter update
```

------------------------------------------------------------------------

# 34. Inference

For an unseen Java program:

``` text
New Java Source
      ↓
Tokenizer
      ↓
Encoder
      ↓
ESS
      ↓
Autoregressive Decoder
      ↓
Predicted Runtime Event Sequence
```

The intended innovation is:

> **No program execution is required during inference.**

This must be distinguished from dataset creation, where JIVE execution
is required to produce ground-truth traces.

------------------------------------------------------------------------

# 35. The Major Unresolved Problem: Very Long Runtime Traces

The runtime statistics show:

``` text
Maximum ≈ 3.69 million tokens
95th percentile ≈ 842,656
99th percentile ≈ 2.09 million
```

Therefore, a standard Transformer cannot simply process the entire trace
as one sequence.

The project should not blindly select a value such as:

``` text
MAX_LENGTH = 512
```

because that would discard large portions of long traces.

------------------------------------------------------------------------

# 36. Recommended Solution: Event-Preserving Chunking

The next major implementation should be:

``` text
SRER
 ↓
Event-Aware Tokenization
 ↓
Event Boundaries
 ↓
Create manageable chunks
 ↓
Transformer
```

The critical rule is:

> **Never split an individual runtime event across two chunks.**

For example:

``` text
Chunk 1:
<EVENT_START> ... <EVENT_END>
<EVENT_START> ... <EVENT_END>
...

Chunk 2:
<EVENT_START> ... <EVENT_END>
...
```

The chunk size, overlap strategy, and context mechanism still need to be
finalized experimentally.

------------------------------------------------------------------------

# 37. Evaluation Layer

The proposed evaluation compares:

``` text
Predicted Runtime Trace
          vs
Ground-Truth Runtime Trace
```

Planned metrics:

### BLEU

Sequence-level similarity.

### Exact Match Accuracy

Whether the complete predicted sequence exactly matches the target.

### Event-Type Accuracy

Whether event categories are correctly predicted.

Examples:

``` text
MC
ME
VW
NO
...
```

### Event-Order Accuracy

Whether predicted events appear in the correct execution order.

This is especially important because execution order is part of runtime
semantics.

------------------------------------------------------------------------

# 38. Performance Analysis

The proposed analysis includes:

### Result analysis

Study overall performance.

### Ablation studies

Remove or modify components and measure the effect.

Potential ablations:

-   no event boundary tokens,
-   no attributes,
-   no ESS,
-   different chunk sizes,
-   different tokenization approaches.

### Error analysis

Identify:

-   wrong event type,
-   missing event,
-   extra event,
-   wrong attribute,
-   wrong order,
-   incorrect termination.

------------------------------------------------------------------------

# 39. What Has Been Completed

## Data preparation

-   Java source collection identified.
-   Matching source files extracted.
-   Abstract execution traces organized.
-   Runtime event names compacted.
-   Source and trace filenames matched.

## Module 3

-   Static source parsing implemented.
-   Static program representations generated.
-   Runtime SRER/ESR generation implemented.
-   Event types normalized.
-   Event attributes preserved.
-   Execution order preserved.
-   Source--trace pairs generated.
-   Pair validation implemented.
-   Empty-trace pair identified and excluded.
-   562 valid pairs obtained.
-   Dataset split into train/validation/test.

## Module 4 preparation

-   Source sequence-length analysis completed.
-   Runtime sequence-length analysis completed.
-   Runtime event-count analysis completed.
-   Event-aware tokenization prototype/analysis completed.
-   Long-runtime-sequence problem identified.

------------------------------------------------------------------------

# 40. Current Quantitative Status

## Dataset

``` text
Valid pairs = 562
Training    = 449
Validation  = 56
Test        = 57
```

## Source tokens

``` text
Minimum = 32
Maximum = 918
Mean    = 128.85
Median  = 90
95th    = 348.6
99th    = 568.72
```

## Runtime tokens

``` text
Minimum = 51
Maximum = 3,692,475
Mean    = 125,061.67
Median  = 4,963
95th    = 842,655.9
99th    = 2,093,079.37
```

## Runtime events

``` text
Minimum = 5
Maximum = 369,211
Mean    = 11,677.61
Median  = 438
95th    = 79,971.45
99th    = 198,256.50
```

------------------------------------------------------------------------

# 41. What Is Yet To Be Done

The following are not yet fully completed and should not be presented as
completed implementation.

## Module 4

1.  Final source tokenizer.
2.  Final event-aware runtime tokenizer.
3.  Vocabulary construction.
4.  Token-to-ID conversion.
5.  Special-token handling.
6.  Event-preserving runtime chunking.
7.  Padding strategy.
8.  Attention mask generation.
9.  Embedding preparation.
10. Positional encoding implementation/verification.

## EST

1.  Transformer encoder.
2.  Precise ESS definition.
3.  ESS implementation.
4.  Transformer decoder.
5.  Masked self-attention.
6.  Encoder-decoder cross-attention.
7.  Autoregressive generation.
8.  `<SOS>` and `<EOS>` handling.
9.  Training loop.
10. Teacher forcing.
11. Cross-entropy loss.
12. Optimizer.
13. Validation.
14. Checkpointing.
15. Inference.

## Evaluation

1.  BLEU implementation.
2.  Exact-match evaluation.
3.  Event-type accuracy.
4.  Event-order accuracy.
5.  Error analysis.
6.  Ablation studies.

------------------------------------------------------------------------

# 42. Important Design Decisions Still To Be Finalized

### SRER naming

The generated files currently say `ESR`, while the methodology uses
`SRER`.

The final project should choose one term and use it consistently.

### Step representation

Decide whether the numeric `step` should be explicitly tokenized or
whether sequence position is sufficient.

### Chunk size

Determine an experimentally justified maximum runtime chunk length.

### Chunk context

Determine whether chunks require overlap or additional context.

### Tokenization

Finalize the exact Java-aware source tokenizer and runtime event-aware
tokenizer.

### Vocabulary

Finalize separate source/runtime vocabularies or a shared strategy.

### ESS

Define precisely how encoder representations are transformed or
interpreted as the Execution Semantic Space.

------------------------------------------------------------------------

# 43. Recommended Next Work Order

The safest implementation order is:

``` text
1. Finalize SRER
       ↓
2. Implement event-aware runtime tokenizer
       ↓
3. Implement event-preserving chunking
       ↓
4. Analyze chunk-length distribution
       ↓
5. Build source/runtime vocabularies
       ↓
6. Generate token IDs
       ↓
7. Add special tokens
       ↓
8. Generate padding and masks
       ↓
9. Build embeddings + positional information
       ↓
10. Implement Transformer encoder
       ↓
11. Implement ESS
       ↓
12. Implement decoder + cross-attention
       ↓
13. Train
       ↓
14. Validate
       ↓
15. Test on unseen programs
       ↓
16. Evaluate
       ↓
17. Ablation + error analysis
```

------------------------------------------------------------------------

# 44. Current Project File Roles

  -----------------------------------------------------------------------
  File                                Purpose
  ----------------------------------- -----------------------------------
  `extract_matching_sources.py`       Finds source files corresponding to
                                      programs with traces

  `compact_traces.py`                 Compacts/normalizes runtime event
                                      names

  `create_esr.py`                     Creates structured runtime event
                                      representation

  `source_parser.py`                  Extracts static Java program
                                      structure

  `create_training_pairs.py`          Pairs source representation with
                                      runtime representation

  `validate_pairs.py`                 Validates source--trace pairs

  `create_training_dataset.py`        Creates consolidated Module 3
                                      dataset

  `inspect_dataset.py`                Inspects dataset structure

  `split_dataset.py`                  Creates train/validation/test sets

  `analyze_lengths.py`                Analyzes source/runtime sequence
                                      lengths

  `analyze_event_lengths.py`          Analyzes runtime event counts

  `tokenize_and_analyze.py`           Prototype/analysis for event-aware
                                      tokenization
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# 45. Module 3 vs Module 4 vs EST

A simple way to understand the project is:

## Module 3 asks:

> **How do we represent runtime behaviour in a structured form?**

Answer:

``` text
JIVE Trace
    ↓
SRER
```

## Module 4 asks:

> **How do we convert those structured representations into
> Transformer-ready numerical inputs?**

Answer:

``` text
SRER
 ↓
Tokenization
 ↓
Vocabulary
 ↓
Token IDs
 ↓
Masks / Embeddings
```

## EST asks:

> **Can the Transformer learn to generate runtime behaviour from source
> code?**

Answer:

``` text
Source
 ↓
Encoder
 ↓
ESS
 ↓
Decoder
 ↓
Predicted Runtime Events
```

------------------------------------------------------------------------

# 46. Final End-to-End Pipeline

``` text
                 IBM Project CodeNet
                          │
                          ▼
                     Java Source
                          │
             ┌────────────┴────────────┐
             │                         │
             ▼                         ▼
      Static Parsing                 JIVE
             │                         │
             ▼                         ▼
 Static Program Representation   Runtime Trace
             │                         │
             │                         ▼
             │                  Trace Compaction
             │                         │
             │                         ▼
             │                        SRER
             │                         │
             └────────────┬────────────┘
                          ▼
                  Source–Trace Pairs
                          │
                          ▼
                       Validation
                          │
                          ▼
                  Train / Val / Test
                          │
                          ▼
                 MODULE 4 INPUT LAYER
                    /                                /                                 ▼                 ▼
          Source Tokenizer    Event-Aware
                              Runtime Tokenizer
                  │                 │
                  ▼                 ▼
            Source Tokens      Runtime Tokens
                  │                 │
                  └──────┬──────────┘
                         ▼
                 Vocabulary + IDs
                         │
                         ▼
              Event-Preserving Chunking
                         │
                         ▼
                    Padding/Masks
                         │
                         ▼
                     Embeddings
                         │
                         ▼
             EXECUTION SEMANTIC
                  TRANSFORMER
                         │
                  ┌──────┴──────┐
                  ▼             ▼
               Encoder       Decoder
                  │             ▲
                  ▼             │
                 ESS ───────────┘
                         │
                         ▼
               Predicted Runtime
                  Event Sequence
                         │
                         ▼
                    Evaluation
                         │
            ┌────────────┼────────────┐
            ▼            ▼            ▼
          BLEU      Exact Match   Event Metrics
                                      │
                                      ▼
                              Error/Ablation
```

------------------------------------------------------------------------

# 47. Complete Project Summary for a New Team Member

The project is building a supervised learning system that learns to
predict Java runtime execution behaviour from Java source code. Java
programs from IBM Project CodeNet are paired with JIVE-generated runtime
traces. Because the original JIVE traces are verbose and flat, the
traces are compacted and converted into a Structured Runtime Event
Representation (SRER), where each event has a step, event type and
event-specific attributes, while execution order is preserved. In
parallel, the Java source is parsed into a static program representation
containing classes, methods, variables, method calls and control
statements. These two representations are matched by program ID and
validated. After removing one unusable empty-trace example, the project
has 562 valid source--runtime pairs, divided into 449 training, 56
validation and 57 test examples.

The initial Module 4 analysis measured the lengths of both source and
runtime sequences. Source sequences are relatively short, but runtime
traces are highly variable, reaching approximately 3.69 million tokens
and hundreds of thousands of events for some programs. This discovery is
important because a conventional Transformer cannot directly process
such enormous traces as one sequence. Therefore, the next critical task
is to implement event-aware runtime tokenization together with
event-preserving chunking. Once manageable sequences are obtained, the
project will construct vocabularies, convert tokens to IDs, add special
tokens, generate padding and attention masks, and prepare embeddings and
positional information.

The final model is planned as an Execution Semantic Transformer (EST).
The encoder receives tokenized Java source and learns contextual source
representations. These representations are intended to form an Execution
Semantic Space (ESS), which captures source information relevant to
runtime behaviour. An autoregressive decoder then uses masked
self-attention and cross-attention to generate the runtime event
sequence. Training will use teacher forcing, cross-entropy loss,
backpropagation and Adam optimization. During inference, the model
should be able to predict runtime behaviour for unseen Java programs
without executing them. Finally, predicted traces will be compared with
ground-truth traces using BLEU, exact match, event-type accuracy and
event-order accuracy, followed by ablation and error analysis.

The most important current achievement is the creation of a validated
**562-pair source--runtime dataset** with preserved runtime event
semantics. The most important remaining technical challenge is the
handling of extremely long runtime traces. The next immediate
implementation task should therefore be **event-aware tokenization
followed by event-preserving chunking**, before the final Transformer
input pipeline and EST model are implemented.
