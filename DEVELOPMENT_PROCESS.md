# DEVELOPMENT_PROCESS

This document describes how I used AI during the Bonita Studio technical challenge: which tools I used, how they were integrated into my workflow, how they influenced navigation and decisions in the codebase, and my overall reflections.

---

## 1. AI tools used

### Tools

- **ChatGPT (GPT-5)**

I worked on my **personal laptop**, where I don’t have IDE-integrated AI tools like Copilot or Claude inside IntelliJ / Eclipse / VS Code.  
Because of that constraint, **ChatGPT was my primary and only AI assistant** for this challenge.

At work I am used to tools like **Copilot** and **Claude** integrated in IDEs, but for this challenge I deliberately relied only on **ChatGPT in the browser**.

### Why I chose this tool

- It was **the only AI tool available** in my setup (no IDE plugins installed on this personal machine).
- I consider current OpenAI models among **the strongest general-purpose LLMs**, especially for reasoning about code and generating implementation proposals.
- I trusted ChatGPT to act as a **very strong “pair-programmer”**, while I kept the responsibility of:
    - understanding the challenge and requirements,
    - validating its answers,
    - and controlling the final code that gets committed.

---

## 2. Workflow documentation

Below are the main implementation steps where I used AI and how.

### Step 1 – Understanding the challenge and the documentation

**Goal:** Get a clear mental model of the challenge and the Bonita ecosystem (Studio, Engine, other components).

**Prompt / question to the AI (simplified example):**

> “Here is the text of the Bonita technical challenge and the documentation links.  
> Extract the essential information I need:
> - how to build and run Bonita Studio,
> - how to build and run Bonita Engine and related components locally,
> - and the key steps I must complete for the challenge.”

**How I used the response:**

- I used the AI’s answer as a **structured summary** of:
    - which repositories are involved,
    - what needs to be built first,
    - and how to run the different parts.
- I **did not copy everything blindly**; I cross-checked against the official documentation and my own reading.

**Reasoning for accepting/rejecting suggestions:**

- I accepted the **high-level structure** provided by the AI (e.g., “build engine with Gradle, publish to local repository, then build Studio with Maven using that repository”).
- When the AI was **too generic** (e.g., “run the project with standard Maven command” without taking into account the specific Tycho / Gradle setup or the custom local repo path), I **rejected or adjusted** those parts and verified commands manually.

**Challenges and how I resolved them:**

- The documentation links were sometimes **generic** and not always directly adapted to the exact version and setup required by the challenge.
- I had to:
    - read the **official docs** myself,
    - combine them with AI summaries,
    - and **experiment locally** until all components (Studio, Engine, etc.) were correctly launched.

---

### Step 2 – Automating local setup with a script (cloning & building repos)

**Goal:** Create a script that clones all required Bonita repositories and builds/publishes them locally in the right order.

**Prompt / question to the AI (simplified example):**

> “I’m working on the Bonita challenge.  
> I have multiple GitHub repos (engine, UI designer, web pages, applications, etc.).  
> Help me write a shell script that:
> 1. Clones each repo from GitHub,
> 2. Runs the correct build command for each (Gradle or Maven),
> 3. Publishes artifacts to my local repository at `.../CommunityRepository` instead of the default `.m2`.”

**How I used the response:**

- I used the AI output as a **starting point** for a `bash` script that:
    - clones the repos,
    - runs the proper `./gradlew build publishToMavenLocal` or `./mvnw clean install` commands,
    - and aims to use a custom local repository.
- I **modified** paths, environment variables and some commands to match the **real Bonita build instructions** and my local file system.

**Reasoning for accepting/rejecting suggestions:**

- I accepted the overall **script structure** (function per repo, loops, error handling, etc.), because it saved me time typing boilerplate.
- I **rejected or rewrote** parts where:
    - the AI hallucinated flags or Gradle options that do not exist,
    - the repository path or publishing location was incorrect,
    - or the order of builds did not match the actual dependencies.

**Challenges and how I resolved them:**

- When asking for a script that integrates deeply with Gradle’s and Maven’s local repository configuration, the AI sometimes **invented configuration parameters**.
- The script also required **fine-tuning** to match:
    - my specific `CommunityRepository` folder,
    - and the exact Bonita build chain.
- I fixed this by:
    - running the script multiple times,
    - reading the **real error messages**,
    - and **correcting the script manually** until the pipeline of repository builds worked.

---

### Step 3 – Implementing the feature changes with AI assistance

**Goal:** Implement the requested feature in the Bonita Studio codebase (for example, around BPMN import, tool detection, dialogs, and logging).

**Prompt / question to the AI (simplified example):**

> “Here is an existing Java class from the Bonita Studio project (code pasted).  
> I need to implement a new behavior when importing BPMN files:
> - detect the source modeling tool from the BPMN `<definitions>` `exporter` and `exporterVersion`,
> - if the info is missing, prompt the user with a dialog listing known tools plus ‘Other’,
> - after import, show a confirmation dialog with the detected or user-provided tool,
> - and log the import event (tool name, version if any, timestamp).  
    > Propose changes that respect the existing architecture and patterns of this code.”

**How I used the response:**

- I used the AI’s code as a **draft implementation**:
    - parsing the XML,
    - adding UI dialogs,
    - adding logging code.
- I then **reviewed the code manually** to align it with:
    - the existing style,
    - the correct packages and services,
    - and any technical constraints already present in the project.

**Reasoning for accepting/rejecting suggestions:**

- I accepted:
    - **core logic** that was consistent with the existing design,
    - well-structured methods (e.g., separated parsing, dialog display, logging).
- I rejected or reworked:
    - suggestions that introduced **new patterns** not used in the rest of the module,
    - any code that ignored existing utility classes or services already available in the codebase,
    - any UI or logging code that did not fit the framework used by Bonita.

**Challenges and how I resolved them:**

- Sometimes the AI proposed:
    - **framework-agnostic Java code**, not fully aligned with Eclipse/Bonita plugin conventions.
- To resolve this, I:
    - navigated the project to find **similar existing implementations**,
    - adjusted the AI-generated code to follow the **same conventions and APIs**,
    - and tested the behavior to ensure that the dialogs and logging were actually triggered in the right flows.

---

### Step 4 – Analysing logs and fixing local run/build issues

**Goal:** Make the full stack run successfully locally (Studio + Engine + other components) and debug issues.

**Prompt / question to the AI (simplified example):**

> “Here is a build/log output from Bonita Studio / Engine (log pasted).  
> Explain what is failing and propose concrete steps to fix it so that I can build and run the project locally.”

**How I used the response:**

- I used the AI mainly to **get hypotheses** about:
    - missing dependencies,
    - misconfigured repositories,
    - or incorrect versions.
- I did **not** rely blindly on its explanations; instead, I **compared** them with what I saw in the logs.

**Reasoning for accepting/rejecting suggestions:**

- I accepted suggestions when they were **consistent with the actual stack trace** and with what I know about Maven/Gradle.
- I rejected suggestions when:
    - the AI clearly **hallucinated** a cause that did not match the error,
    - or proposed changes that were too generic or unrelated to Bonita’s specific build (for example, proposing an arbitrary dependency version that didn’t exist in the actual repository).

**Challenges and how I resolved them:**

- On log analysis and script-generation around local run, the AI sometimes:
    - **invented wrong file paths or commands**,
    - misinterpreted the real error messages.
- In those cases, I:
    - read and interpreted the logs **myself**,
    - identified the **real root cause**,
    - and then either:
        - corrected the configuration manually, or
        - gave the AI a **more precise description** of the situation so that its next suggestion would be closer to reality.

---

## 3. Code navigation strategy

### How I used AI to understand the existing codebase

- I used AI mainly when I had a **specific class or snippet** and I wanted:
    - a quick explanation of its responsibility,
    - or help to integrate a new behavior without breaking existing contracts.
- Typical usage pattern:
    - Navigate the code in the IDE (search by package, class name, or text).
    - When I find a relevant class, **copy key parts** into ChatGPT.
    - Ask: *“What is this class doing? Where is the best place to plug in [new feature]?”*

The AI acted as a **“fast explainer”** for complex or unfamiliar parts of the code, but I always cross-checked by reading the code myself.

### Techniques that helped me locate the right integration points

- **Classic IDE search first**, AI second:
    - Use search by symbol / string to find where imports, dialogs or logs are already handled.
    - Look at existing flows handling similar features.
- Once I have 1–2 candidate classes:
    - Paste the relevant code in ChatGPT and ask:
        - if this is a good place to integrate,
        - and what side effects I should be careful about.
- I also used the AI to:
    - propose **clean refactorings** or new methods when existing methods were already large or complex.

### Effective prompts (examples)

- *“Here is the existing import workflow code and the new requirement. Suggest where to hook in the detection of the BPMN source tool so that it stays consistent with the current design.”*
- *“Given this logging utility class and this new use case, propose a new log entry method that follows the same conventions.”*

These prompts worked well because they provided **concrete context** (real classes and methods) instead of asking generic questions.

### Less effective / ineffective prompts

- High-level prompts like:
    - *“Why doesn’t my project start?”* with only a partial or vague log,
    - or *“Generate a full script to set up everything locally”* without concrete paths.
- In those cases the AI tended to **hallucinate**:
    - non-existent paths,
    - or generic steps not adapted to Bonita’s ecosystem.

I learned that the more **precise and contextual** the prompt (logs, file paths, concrete snippets), the more useful the answer.

---

## 4. Decision points

### Decision 1 – Relying on AI for code generation instead of writing everything manually

- I consciously chose to **let the AI generate most of the code** (e.g., parsing logic, dialog handling, logging blocks), because I know:
    - the model can produce **very good quality Java**,
    - and I can **review and understand** the code (I have strong Java 17 skills).
- My role was to:
    - **design good prompts**,
    - validate architecture and readability,
    - and adapt the code to the specific frameworks used in the project.

### Decision 2 – Not trusting AI blindly and manually validating every critical part

- Even if I consider AI as part of the “top 1% coders”, I **never trusted the output blindly**.
- For each important suggestion, I:
    - checked if it matched the requirements,
    - verified that it followed the project’s conventions,
    - and adjusted or simplified when necessary.
- In some cases, instead of accepting a big block of generated code, I **asked for smaller variants** and composed my own final version.

### Decision 3 – Disagreeing with AI on scripts and log interpretation

- When generating scripts for running the project locally, the AI sometimes:
    - proposed unrealistic commands,
    - misinterpreted Gradle/Maven errors,
    - or suggested adding dependencies that were not present in the actual repositories.
- In those situations, I **explicitly disagreed** with the suggestions and:
    - relied on my own analysis of the logs,
    - updated the script or configuration by hand,
    - and only used the AI for **clarification, not for the final decision**.

---

## 5. Reflection

### What AI did well in this challenge

- **Accelerated coding time**:
    - It saved me a lot of time typing boilerplate and routine code.
    - It helped generate structured solutions quickly (methods, dialog flows, logging logic).
- **Contextual reasoning on provided code**:
    - When I pasted real code snippets, the AI gave good explanations and refactoring ideas.
- **Support for understanding the ecosystem**:
    - Summarized documentation and build steps in a more digestible way, especially at the beginning.

### Where AI struggled or gave incorrect/weak suggestions

- **Local run & script generation**:
    - When I asked for a “universal script” to run everything locally, the AI sometimes hallucinated:
        - non-existent flags,
        - wrong paths,
        - or incorrect assumptions about how Bonita is built.
- **Log analysis**:
    - For some complex logs, it gave **plausible but incorrect explanations**.
    - I had to read and interpret the logs myself, then constrain and correct the AI.

### What I would do differently next time

- Use **IDE-integrated AI tools** (like Copilot or Claude-based plugins) when possible, so that:
    - the AI has **direct access to the codebase**,
    - suggestions are better anchored in the real project context,
    - I don’t need to copy/paste as much code into the chat.
- Keep the same philosophy:
    - use AI as a **very strong assistant and code generator**,
    - but remain responsible for **architecture decisions, validation, and debugging**.
- Be even more **systematic** in:
    - saving effective prompts,
    - and documenting precisely when I accept or reject AI suggestions, so that the process is fully reproducible.

---