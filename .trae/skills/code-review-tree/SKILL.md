---
name: "code-review-tree"
description: "Decision-tree based code review skill. Invoke when user requests code review, after implementing features, or before committing changes."
---

# Decision Tree Code Review

A systematic code review approach using decision trees to ensure comprehensive coverage of code quality aspects.

## Quick Mode vs Full Mode

| Mode | When to Use | Checks |
|------|-------------|--------|
| **Quick** | Small changes, single file, quick feedback | 1, 2, 4 |
| **Full** | PR review, major feature, before merge | 1-7 all |

---

## Decision Tree Checks

### 1. FUNCTIONALITY (CRITICAL)
| Check | Question | Fail Action |
|-------|----------|-------------|
| F1 | Does code do what it's supposed to do? | [CRITICAL] Fix functionality |
| F2 | Are edge cases handled (empty, null, boundary)? | [HIGH] Add edge case handling |
| F3 | Are error states handled gracefully? | [HIGH] Add error handling |

### 2. CODE QUALITY (MEDIUM)
| Check | Question | Fail Action |
|-------|----------|-------------|
| CQ1 | Is naming clear and consistent? | [MEDIUM] Improve naming |
| CQ2 | Is there code duplication >3 lines? | [MEDIUM] Extract to function |
| CQ3 | Are functions <30 lines? | [MEDIUM] Break into smaller |
| CQ4 | Is there dead/unused code? | [LOW] Remove dead code |

### 3. ARCHITECTURE (HIGH)
| Check | Question | Fail Action |
|-------|----------|-------------|
| A1 | Does it follow Clean Architecture? | [HIGH] Refactor layers |
| A2 | Does it follow MVVM pattern? | [HIGH] Refactor to MVVM |
| A3 | Is module boundary respected? | [HIGH] Fix module dependency |
| A4 | Is separation of concerns maintained? | [MEDIUM] Refactor separation |

### 4. SECURITY (CRITICAL)
| Check | Question | Fail Action |
|-------|----------|-------------|
| S1 | Any hardcoded secrets/keys? | [CRITICAL] Move to secure storage |
| S2 | Is user input validated? | [HIGH] Add validation |
| S3 | Are sensitive data encrypted? | [CRITICAL] Add encryption |

### 5. PERFORMANCE (MEDIUM)
| Check | Question | Fail Action |
|-------|----------|-------------|
| P1 | Any unnecessary recompositions? | [MEDIUM] Optimize Compose |
| P2 | Any memory leaks potential? | [HIGH] Fix leak source |
| P3 | Are coroutines properly scoped? | [HIGH] Fix coroutine scope |
| P4 | Any blocking main thread? | [HIGH] Move to IO dispatcher |

### 6. TESTING (MEDIUM)
| Check | Question | Fail Action |
|-------|----------|-------------|
| T1 | Are there unit tests? | [MEDIUM] Add unit tests |
| T2 | Do tests cover critical paths? | [MEDIUM] Add test coverage |

### 7. DOCUMENTATION (LOW)
| Check | Question | Fail Action |
|-------|----------|-------------|
| D1 | Is complex logic documented? | [LOW] Add KDoc |
| D2 | Are public APIs documented? | [LOW] Add KDoc |

---

## Project-Specific Rules (AllAiNovel)

### Module Structure Check
```
✅ Correct: feature-* → domain → data → core-*
❌ Wrong: feature-* → data (skip domain)
❌ Wrong: core-* → feature-* (reverse dependency)
```

### Naming Convention Check
| Type | Convention | Example |
|------|------------|---------|
| Package | lowercase | `com.cycling.feature.worldbuilding` |
| Class | PascalCase | `WorldSettingListScreen` |
| Function | camelCase | `navigateToWorldSettingList` |
| Variable | camelCase | `bookId` |
| Constant | SCREAMING_SNAKE | `MAX_RETRY_COUNT` |

### Kotlin/Android Specific Checks
| Check | Pattern | Anti-Pattern |
|-------|---------|--------------|
| Null Safety | `?.let` / `?:` | `!!` force unwrap |
| Coroutines | `viewModelScope` | `GlobalScope` |
| Compose State | `collectAsStateWithLifecycle` | `collectAsState` |
| DI | `@HiltViewModel` | Manual construction |
| Navigation | Type-safe routes (`@Serializable`) | String-based routes |

---

## Severity Actions

| Level | Action | Auto-Fix |
|-------|--------|----------|
| **CRITICAL** | Must fix before any merge | Provide fix template |
| **HIGH** | Should fix before merge | Provide suggestion |
| **MEDIUM** | Fix in follow-up PR | List in recommendations |
| **LOW** | Optional improvement | Mention in summary |

---

## Output Format

```markdown
## Code Review Report

### Summary
- Files reviewed: [count]
- Mode: Quick/Full
- Issues: 🔴 CRITICAL: [n] | 🟠 HIGH: [n] | 🟡 MEDIUM: [n] | ⚪ LOW: [n]

### Decision Tree Results

| Check | Status | Notes |
|-------|--------|-------|
| F1 | ✅/❌ | [details] |
| ... | ... | ... |

### Findings

#### [FileName.kt](file:///path/to/File.kt#L[line])
| Line | Severity | Issue | Suggestion |
|------|----------|-------|------------|
| 42 | 🔴 CRITICAL | Hardcoded API key | Use BuildConfig or EncryptedDataStore |

```kotlin
// ❌ Before
val apiKey = "sk-xxx"

// ✅ After
val apiKey = BuildConfig.API_KEY
```

### Recommendations
1. **[Priority]** [Action item with file reference]

### Verdict
- [ ] ✅ Approved - Ready to merge
- [ ] ⚠️ Conditional - Fix HIGH issues first
- [ ] ❌ Blocked - CRITICAL issues must resolve
```

---

## Common Fix Templates

### Missing KDoc
```kotlin
/**
 * [Brief description].
 *
 * @param paramName [description]
 * @return [description]
 */
```

### Missing Null Check
```kotlin
// Before
val result = data!!.value

// After
val result = data?.value ?: return@let
```

### Missing Coroutine Scope
```kotlin
// Before
GlobalScope.launch { ... }

// After
viewModelScope.launch { ... }
```

### Compose State Collection
```kotlin
// Before
val state by viewModel.state.collectAsState()

// After
val state by viewModel.state.collectAsStateWithLifecycle()
```

---

## Usage

When invoked:
1. **Detect scope** - Single file → Quick Mode | Multiple files/PR → Full Mode
2. **Run decision tree** - Execute checks in priority order
3. **Document findings** - Include code references and fix templates
4. **Provide verdict** - Clear merge-readiness assessment
5. **Offer fixes** - Auto-generate fix suggestions where applicable
