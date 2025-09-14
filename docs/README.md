# Buddy User Guide 🚀

Welcome to **Buddy** - your enthusiastic productivity companion! 🎉

![Buddy Screenshot](Ui.png)

Buddy is a personal task management application designed to help you conquer your to-do list with style and motivation! With an intuitive interface and enthusiastic personality, Buddy makes task management fun and engaging.

## Features ✨

### Adding Todos 📝

Create simple todo tasks for things you need to do.

**Command Format:** `todo <description>`

**Example:**
```
todo buy groceries
```

**Expected Output:**
```
Excellent choice! 🌟 Your new adventure awaits:
  [T][ ] buy groceries
Quest log updated! You now have 1 tasks to conquer. Let's do this! 🚀
```

### Adding Deadlines ⏰

Create tasks with specific deadlines.

**Command Format:** `deadline <description> /by <date/time>`

**Examples:**
```
deadline submit report /by 2024-12-01
deadline finish homework /by tomorrow 2pm
```

**Expected Output:**
```
Time-sensitive mission accepted! ⏰ I've logged this deadline:
  [D][ ] submit report (by: 2024-12-01)
You've got 2 tasks in your arsenal now. Beat that clock! ⚡
```

### Adding Events 🎪

Create events with start and end times.

**Command Format:** `event <description> /from <start> /to <end>`

**Examples:**
```
event team meeting /from 2pm /to 4pm
event birthday party /from Saturday 7pm /to Sunday 1am
```

**Expected Output:**
```
Fantastic event scheduled! 🎪 Mark your calendar for:
  [E][ ] team meeting (from: 2pm to: 4pm)
Your event lineup now has 3 items! Time to plan and shine! ✨
```

### Listing Tasks 📋

View all your tasks in a beautiful list.

**Command:** `list`

**Expected Output:**
```
Behold! Your magnificent task collection: 📋✨
1.[T][ ] buy groceries
2.[D][ ] submit report (by: 2024-12-01)
3.[E][ ] team meeting (from: 2pm to: 4pm)
```

### Marking Tasks as Done ✅

Celebrate your achievements by marking tasks as completed!

**Command Format:** `mark <task number>`

**Example:**
```
mark 1
```

**Expected Output:**
```
Woohoo! 🎉 Another victory achieved! I've marked this champion as DONE:
  [T][X] buy groceries
You're absolutely crushing it! Keep up the fantastic work! 💪
```

### Unmarking Tasks 🔄

Made a mistake? No worries! Unmark completed tasks.

**Command Format:** `unmark <task number>`

**Example:**
```
unmark 1
```

**Expected Output:**
```
No worries! 🔄 Sometimes we need a do-over. I've unmarked this task:
  [T][ ] buy groceries
Second chances are what make us stronger! You've got this! 💪
```

### Deleting Tasks 🗑️

Remove tasks you no longer need.

**Command Format:** `delete <task number>`

**Example:**
```
delete 2
```

**Expected Output:**
```
Poof! ✨ Task vanished into the digital void! Removed:
  [D][ ] submit report (by: 2024-12-01)
You now have 2 tasks in your quest log. Stay focused, champion! 🎯
```

### Finding Tasks 🔍

Search for tasks containing specific keywords.

**Command Format:** `find <keyword>`

**Example:**
```
find meeting
```

**Expected Output:**
```
Detective work complete! 🕵️‍♂️ Found these matching treasures:
1.[E][ ] team meeting (from: 2pm to: 4pm)
```

### Sorting Tasks 🗂️

Organize your tasks by different criteria.

**Command Formats:**
- `sort description` - Sort alphabetically by task description
- `sort status` - Sort by completion status
- `sort type` - Sort by task type (Todo, Deadline, Event)

**Example:**
```
sort status
```

**Expected Output:**
```
Boom! 💥 Tasks perfectly organized by status! Your list is now a masterpiece
of efficiency! Marie Kondo would be proud! ✨
```

### Exiting the Application 👋

**Command:** `bye`

**Expected Output:**
```
Farewell, my friend! 👋 Keep being awesome, and come back anytime! 🌟
```

## Tips for Success 💡

1. **Be Specific**: Use clear, descriptive task names
2. **Set Realistic Deadlines**: Don't overwhelm yourself
3. **Regular Review**: Use `list` frequently to stay on track
4. **Celebrate Wins**: Mark tasks as done to feel accomplished!
5. **Stay Organized**: Use `sort` to organize your tasks

## Error Handling 🛠️

Buddy provides helpful error messages with suggestions:
- Empty commands get friendly prompts
- Invalid task numbers receive helpful guidance
- Malformed dates/times get format examples
- All errors include emoji and encouraging language!

## Technical Notes 🔧

- Tasks are automatically saved to `./data/buddy.txt`
- The application supports window resizing
- Error messages are highlighted in red for visibility
- All task numbers start from 1 (user-friendly indexing)

---

Made with ❤️ and lots of ☕ for your productivity journey!