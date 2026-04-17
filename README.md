# Task Manager - UI Testing Evaluation Project

## Overview
This is a **Task Manager** Android application designed specifically for evaluating candidates' ability to write comprehensive UI tests using Espresso. The app has moderate complexity with multiple screens, various UI states, and real-world features that are perfect for testing.

## Important: No Tests Included
This project intentionally contains **NO UI TESTS**. The candidate will implement Espresso tests from scratch to validate the app's functionality.

## Project Details

### Tech Stack
- **Language**: Mixed Java + Kotlin codebase
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt (Dagger-Hilt)
- **State Management**: LiveData and StateFlow
- **UI**: View Binding, RecyclerView, Material Design 3
- **Data Layer**: Fake/Mock data source (no real API)
- **Min SDK**: 28 (Android 9.0)
- **Target SDK**: 36

### App Features

#### 1. Task List Screen (Kotlin)
**File**: `TaskListActivity.kt`

Features:
- RecyclerView displaying a list of tasks
- Real-time search functionality
- Multiple UI states: Loading, Success, Error, Empty
- Pull-to-refresh behavior
- FAB (Floating Action Button) for adding new tasks
- Task completion checkbox
- Navigation to detail screen

Key View IDs for Testing:
- `recycler_view_tasks` - Main task list
- `search_view` - Search input
- `button_clear_search` - Clear search button
- `fab_add_task` - Add task FAB
- `progress_bar` - Loading indicator
- `layout_empty` - Empty state layout
- `layout_error` - Error state layout
- `button_retry` - Retry button

#### 2. Task Detail Screen (Java)
**File**: `TaskDetailActivity.java`

Features:
- Displays full task information
- Toggle favorite status
- Edit task navigation
- Delete task with confirmation dialog
- Back navigation

Key View IDs for Testing:
- `text_detail_title` - Task title
- `text_detail_description` - Task description
- `text_detail_category` - Task category
- `text_detail_priority` - Priority level
- `text_detail_status` - Completion status
- `button_toggle_favorite` - Favorite toggle
- `button_edit_task` - Edit button
- `button_delete_task` - Delete button

#### 3. Task Form Screen (Kotlin)
**File**: `TaskFormActivity.kt`

Features:
- Add new tasks
- Edit existing tasks
- Form validation with error messages
- Category dropdown (Spinner)
- Priority dropdown (Spinner)
- Date picker for due date
- Real-time validation feedback

Key View IDs for Testing:
- `edit_text_title` - Title input
- `edit_text_description` - Description input
- `spinner_category` - Category selector
- `spinner_priority` - Priority selector
- `edit_text_due_date` - Due date picker
- `button_save` - Save button
- `text_input_layout_title` - Title container (for error)
- `text_input_layout_description` - Description container (for error)

### Data Models

#### Task
```kotlin
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val priority: TaskPriority,
    val isCompleted: Boolean,
    val isFavorite: Boolean,
    val dueDate: String?
)
```

#### TaskCategory Enum
- WORK
- PERSONAL
- SHOPPING
- HEALTH
- OTHER

#### TaskPriority Enum
- HIGH (Red - #D32F2F)
- MEDIUM (Orange - #F57C00)
- LOW (Green - #388E3C)

### Architecture Overview

```
app/
├── data/
│   ├── model/
│   │   ├── Task.kt
│   │   ├── UiState.kt
│   ├── repository/
│   │   ├── TaskRepository.kt (interface)
│   │   └── TaskRepositoryImpl.kt
│   └── source/
│       └── FakeTaskDataSource.kt
├── di/
│   └── AppModule.kt (Hilt module)
└── ui/
    ├── list/
    │   ├── TaskListActivity.kt
    │   ├── TaskListViewModel.kt
    │   └── TaskListAdapter.kt
    ├── detail/
    │   ├── TaskDetailActivity.java
    │   └── TaskDetailViewModel.kt
    └── form/
        ├── TaskFormActivity.kt
        └── TaskFormViewModel.kt
```

### Testability Features

1. **Unique View IDs**: All important UI elements have stable, descriptive IDs
2. **Dependency Injection**: Hilt makes it easy to inject mock repositories
3. **Fake Data Source**: `FakeTaskDataSource` provides predictable test data
4. **UI States**: Clear separation of Loading, Success, Error, and Empty states
5. **No Hardcoded Delays**: Simulated network delays can be controlled
6. **ViewModel Logic**: Business logic is separated from UI
7. **Deterministic Behavior**: UI states are predictable and testable

### Sample Test Scenarios

Candidates should implement tests for:

#### List Screen Tests
- ✅ Display tasks in RecyclerView
- ✅ Search functionality filters tasks
- ✅ Clear search button works
- ✅ FAB navigates to add task screen
- ✅ Clicking task item navigates to detail screen
- ✅ Toggling checkbox marks task complete
- ✅ Loading state shows progress bar
- ✅ Empty state shows empty message
- ✅ Error state shows error message and retry button

#### Detail Screen Tests
- ✅ Display task details correctly
- ✅ Toggle favorite status
- ✅ Navigate to edit screen
- ✅ Delete task with confirmation
- ✅ Back navigation works
- ✅ Priority colors display correctly

#### Form Screen Tests
- ✅ Title validation (required, min 3 chars, max 100 chars)
- ✅ Description validation (required, min 10 chars, max 500 chars)
- ✅ Category selection works
- ✅ Priority selection works
- ✅ Date picker works
- ✅ Save creates new task
- ✅ Edit updates existing task
- ✅ Error messages display for invalid input
- ✅ Form clears validation errors on fix

#### Integration Tests
- ✅ Create task → View in list → Open detail → Edit → Verify changes
- ✅ Create task → Mark complete → Verify status
- ✅ Create task → Delete → Verify removal
- ✅ Search tasks → Verify filtered results

### Running the App

1. **Build the project**:
   ```bash
   ./gradlew assembleDebug
   ```

2. **Install on device/emulator**:
   ```bash
   ./gradlew installDebug
   ```

3. **Run the app**:
   - Launch "Task Manager" from the app drawer
   - The app will load with 8 sample tasks

### Evaluation Criteria

Candidates will be evaluated on:

1. **Test Coverage**: Comprehensive coverage of all features
2. **Test Quality**: Proper use of Espresso matchers, actions, and assertions
3. **Test Organization**: Well-structured test classes and methods
4. **IdlingResources**: Proper handling of asynchronous operations
5. **Edge Cases**: Testing error states, empty states, validation
6. **Code Readability**: Clear test names and good documentation
7. **Best Practices**: Following Android testing best practices

### Sample Data

The app includes 8 pre-populated tasks with various:
- Categories (Work, Shopping, Health, Personal)
- Priorities (High, Medium, Low)
- Completion states (Complete/Pending)
- Favorite status
- Due dates (some with, some without)

## Notes for Interviewers

- The app is fully functional and can be run on any Android device or emulator
- All dependencies are properly configured
- No UI tests are included - candidates start from scratch
- The fake data source allows for predictable testing
- Error states can be triggered through the repository
- All view IDs follow a consistent naming convention for easy testing

---

**Built with**: Android Studio | Kotlin 2.0.21 | AGP 8.13.2