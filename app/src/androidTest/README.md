# Instrumented Tests - Task Manager App

## Overview
This directory contains comprehensive Espresso UI tests for the Task Manager application. The tests are organized by feature and include unit tests for individual screens as well as end-to-end integration tests.

## Test Structure

```
androidTest/
├── HiltTestRunner.kt                    # Custom test runner for Hilt DI
├── EndToEndTest.kt                      # Integration tests across screens
├── ui/
│   ├── list/
│   │   └── TaskListActivityTest.kt      # Task List screen tests
│   ├── detail/
│   │   └── TaskDetailActivityTest.kt    # Task Detail screen tests
│   └── form/
│       └── TaskFormActivityTest.kt      # Task Form screen tests
└── utils/
    ├── CustomMatchers.kt                # Custom Espresso matchers
    └── RecyclerViewActions.kt           # Custom RecyclerView actions
```

## Test Coverage

### TaskListActivityTest (40+ tests)
- ✅ Display tests (toolbar, RecyclerView, FAB, search)
- ✅ Task list rendering with sample data
- ✅ Search functionality (filtering, case-insensitive)
- ✅ Clear search button
- ✅ Navigation to detail screen
- ✅ Navigation to add task screen
- ✅ RecyclerView interactions and scrolling
- ✅ UI states (Loading, Empty, Error)
- ✅ Edge cases (multiple searches, special characters)

### TaskDetailActivityTest (30+ tests)
- ✅ Task details display (title, description, category, priority, status, due date)
- ✅ Favorite/non-favorite task indication
- ✅ Edit button navigation
- ✅ Delete confirmation dialog
- ✅ Cancel delete action
- ✅ Favorite toggle
- ✅ Back navigation
- ✅ Priority color display
- ✅ Different task types (completed, pending, with/without dates)
- ✅ Loading and error states

### TaskFormActivityTest (40+ tests)
- ✅ Form field display
- ✅ Add mode vs Edit mode
- ✅ Title validation (required, min/max length)
- ✅ Description validation (required, min/max length)
- ✅ Real-time validation error clearing
- ✅ Category spinner selection
- ✅ Priority spinner selection
- ✅ Date picker functionality
- ✅ Clear date button
- ✅ Character counters
- ✅ Save functionality
- ✅ Pre-loading data in edit mode
- ✅ Multiple validation errors shown simultaneously

### EndToEndTest (15+ tests)
- ✅ Complete create task flow
- ✅ View task details flow
- ✅ Edit task flow
- ✅ Delete task flow
- ✅ Search and view flow
- ✅ Multiple operations (create → view → edit → delete)
- ✅ Form validation with correction
- ✅ Toggle favorite flow
- ✅ Cancel operations
- ✅ Search edge cases

## Running the Tests

### Run all instrumented tests:
```bash
./gradlew connectedAndroidTest
```

### Run tests for a specific screen:
```bash
# Task List tests
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.marchcode.evaluateuitest.ui.list.TaskListActivityTest

# Task Detail tests
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.marchcode.evaluateuitest.ui.detail.TaskDetailActivityTest

# Task Form tests
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.marchcode.evaluateuitest.ui.form.TaskFormActivityTest

# End-to-end tests
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.marchcode.evaluateuitest.EndToEndTest
```

### Run a specific test:
```bash
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.marchcode.evaluateuitest.ui.list.TaskListActivityTest#taskList_showsSampleTasks
```

### Run tests from Android Studio:
1. Right-click on a test class or method
2. Select "Run 'TestName'"
3. View results in the Run window

## Custom Test Utilities

### CustomMatchers
Provides custom Espresso matchers for common UI patterns:

- `hasTextInputLayoutError(expectedError: String)` - Matches TextInputLayout with specific error
- `hasNoTextInputLayoutError()` - Matches TextInputLayout with no error
- `hasItemCount(expectedCount: Int)` - Matches RecyclerView with specific item count
- `isNotEmpty()` - Matches non-empty RecyclerView

**Usage:**
```kotlin
onView(withId(R.id.text_input_layout_title))
    .check(matches(hasTextInputLayoutError("Title is required")))

onView(withId(R.id.recycler_view_tasks))
    .check(matches(hasItemCount(8)))
```

### RecyclerViewActions
Custom ViewActions for RecyclerView testing:

- `scrollToPosition(position: Int)` - Scrolls to specific position
- `getItemCount(callback: (Int) -> Unit)` - Gets item count

**Usage:**
```kotlin
onView(withId(R.id.recycler_view_tasks))
    .perform(RecyclerViewActions.scrollToPosition(5))
```

## Test Best Practices Used

1. **AAA Pattern**: Arrange, Act, Assert in all tests
2. **Clear Test Names**: Descriptive names following `what_when_then` pattern
3. **Hilt Integration**: Uses HiltTestRunner for dependency injection
4. **Custom Matchers**: Reusable matchers for common assertions
5. **Page Object Pattern**: Helper functions for launching activities
6. **Independent Tests**: Each test is self-contained and can run independently
7. **Comprehensive Coverage**: Testing happy paths, edge cases, and error states
8. **Real User Flows**: End-to-end tests simulate actual user behavior

## Dependencies

All testing dependencies are configured in `app/build.gradle.kts`:

- Espresso Core 3.6.1
- Espresso Contrib 3.6.1
- Espresso Intents 3.6.1
- AndroidX Test Runner 1.6.2
- AndroidX Test Rules 1.6.1
- Hilt Testing 2.51.1
- Truth Assertions 1.4.4
- Coroutines Test 1.9.0

## Test Data

Tests use the 8 pre-populated sample tasks:
1. Complete Project Proposal (Work, High, Not Complete, Favorite)
2. Buy Groceries (Shopping, Medium, Not Complete)
3. Schedule Dentist Appointment (Health, Medium, Complete)
4. Review Code Pull Requests (Work, High, Not Complete, Favorite)
5. Plan Weekend Trip (Personal, Low, Not Complete, Favorite)
6. Update Resume (Personal, Low, Not Complete)
7. Attend Team Meeting (Work, Medium, Complete)
8. Fix Production Bug (Work, High, Not Complete, Favorite)

## Test Reports

After running tests, view detailed reports at:
```
app/build/reports/androidTests/connected/index.html
```

## Continuous Integration

These tests can be integrated into CI/CD pipelines:

```yaml
# Example GitHub Actions
- name: Run Instrumented Tests
  uses: reactivecircus/android-emulator-runner@v2
  with:
    api-level: 29
    script: ./gradlew connectedAndroidTest
```

## Troubleshooting

### Tests fail with "No activities found"
- Ensure the device/emulator is unlocked
- Check that the app is installed correctly

### Hilt errors
- Verify HiltTestRunner is configured in build.gradle
- Ensure @HiltAndroidTest annotation is present

### Element not found errors
- Check view IDs match between layouts and tests
- Verify element is visible (not scrolled off screen)
- Use IdlingResources for async operations if needed

## Future Enhancements

Potential additions for even more comprehensive testing:
- [ ] Screenshot tests with Screenshot Testing library
- [ ] Performance tests with Benchmark library
- [ ] Accessibility tests with Espresso accessibility checks
- [ ] Network condition tests (offline mode)
- [ ] Database state verification tests
- [ ] Animation/transition tests
- [ ] Multi-language tests

---

**Total Test Count**: 125+ tests
**Test Coverage**: All screens, all major features, integration flows
**Maintenance**: Tests are designed to be maintainable and easy to update