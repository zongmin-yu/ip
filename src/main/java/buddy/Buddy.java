package buddy;

import java.util.stream.IntStream;

/**
 * Main class for the Buddy task management application.
 * Provides a command-line interface for managing personal tasks including
 * todos, deadlines, and events with persistent storage.
 */
public class Buddy {
    private static final String UNKNOWN_COMMAND_MESSAGE = "Oops! 🤔 That's a mystery command to me! Try 'list', 'todo', 'deadline', or 'event'. I'm here to help! ✨";
    private static final String BYE_MESSAGE = "Farewell, my friend! 👋 Keep being awesome, and come back anytime! 🌟";
    private static final String WELCOME_MESSAGE = "Hey there! 🎉 I'm Buddy, your enthusiastic task companion!\n" +
                                                  "Ready to conquer your to-do list together? Let's make productivity fun! 🚀";
    
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    
    /**
     * Constructs a new Buddy application with default data file path.
     * Used for GUI mode.
     */
    public Buddy() {
        this("./data/buddy.txt");
    }

    /**
     * Constructs a new Buddy application with the specified data file path.
     * Initializes UI, storage, and task list components.
     * 
     * @param filePath the path to the data file for persistent storage
     */
    public Buddy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (BuddyException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }
    
    /**
     * Starts the main application loop.
     * Displays welcome message and processes user commands until exit.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                String command = Parser.parseCommand(input);
                
                if (command.equals("bye")) {
                    isExit = true;
                    ui.showGoodbye();
                } else {
                    handleCommand(input, command);
                }
            } catch (BuddyException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.close();
    }
    
    private void handleCommand(String input, String command) throws BuddyException {
        switch (command) {
            case "list":
                ui.showTaskList(tasks.getTaskArray(), tasks.size());
                break;
            case "mark":
                handleMark(input);
                break;
            case "unmark":
                handleUnmark(input);
                break;
            case "delete":
                handleDelete(input);
                break;
            case "todo":
                handleTodo(input);
                break;
            case "deadline":
                handleDeadline(input);
                break;
            case "event":
                handleEvent(input);
                break;
            case "find":
                handleFind(input);
                break;
            case "sort":
                handleSort(input);
                break;
            default:
                throw new BuddyException(UNKNOWN_COMMAND_MESSAGE);
        }
    }
    
    private void handleMark(String input) throws BuddyException {
        int index = Parser.parseTaskNumber(input, "mark");
        Task task = tasks.getTask(index);
        tasks.markTask(index);
        saveTasksToFile();
        ui.showTaskMarkedDone(task);
    }
    
    private void handleUnmark(String input) throws BuddyException {
        int index = Parser.parseTaskNumber(input, "unmark");
        Task task = tasks.getTask(index);
        tasks.unmarkTask(index);
        saveTasksToFile();
        ui.showTaskMarkedNotDone(task);
    }
    
    private void handleDelete(String input) throws BuddyException {
        int index = Parser.parseTaskNumber(input, "delete");
        Task task = tasks.getTask(index);
        tasks.deleteTask(index);
        saveTasksToFile();
        ui.showTaskDeleted(task, tasks.size());
    }
    
    private void handleTodo(String input) throws BuddyException {
        String description = Parser.parseDescription(input, "todo");
        Task task = new Todo(description);
        tasks.addTask(task);
        saveTasksToFile();
        ui.showTaskAdded(task, tasks.size());
    }
    
    private void handleDeadline(String input) throws BuddyException {
        String[] parts = Parser.parseDeadline(input);
        Task task = new Deadline(parts[0], parts[1]);
        tasks.addTask(task);
        saveTasksToFile();
        ui.showTaskAdded(task, tasks.size());
    }
    
    private void handleEvent(String input) throws BuddyException {
        String[] parts = Parser.parseEvent(input);
        Task task = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(task);
        saveTasksToFile();
        ui.showTaskAdded(task, tasks.size());
    }
    
    private void handleFind(String input) throws BuddyException {
        String keyword = Parser.parseDescription(input, "find");
        Task[] matchingTasks = tasks.findTasks(keyword);
        ui.showFoundTasks(matchingTasks);
    }
    
    private void handleSort(String input) throws BuddyException {
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2) {
            throw new BuddyException("Please specify sort criteria: 'sort description', 'sort status', or 'sort type'");
        }
        
        String sortBy = parts[1].toLowerCase();
        switch (sortBy) {
            case "description":
                tasks.sortByDescription();
                break;
            case "status":
                tasks.sortByStatus();
                break;
            case "type":
                tasks.sortByType();
                break;
            default:
                throw new BuddyException("Invalid sort criteria. Use: description, status, or type");
        }
        
        saveTasksToFile();
        ui.showTasksSorted(sortBy);
    }

    private void saveTasksToFile() throws BuddyException {
        storage.save(tasks.getTasks());
    }

    /**
     * Initializes the application for GUI mode.
     */
    public void initializeWithGui() {
        // GUI mode initialization - no Scanner needed
    }

    /**
     * Gets the welcome message for GUI display.
     * 
     * @return the welcome message as a string
     */
    public String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }

    /**
     * Processes user input and returns the appropriate response.
     * 
     * @param input the user's input command
     * @return the response message
     */
    public String getResponse(String input) {
        try {
            String command = Parser.parseCommand(input);
            return processCommand(command, input);
        } catch (BuddyException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "🚨 Oops! Something unexpected happened! Please try again. " +
                   "If this keeps happening, maybe try a simpler command first! 🔧";
        }
    }
    
    private String processCommand(String command, String input) throws BuddyException {
        switch (command) {
            case "bye":
                return BYE_MESSAGE;
            case "list":
                return getTaskListResponse();
            case "mark":
                return handleMarkResponse(input);
            case "unmark":
                return handleUnmarkResponse(input);
            case "delete":
                return handleDeleteResponse(input);
            case "todo":
                return handleTodoResponse(input);
            case "deadline":
                return handleDeadlineResponse(input);
            case "event":
                return handleEventResponse(input);
            case "find":
                return handleFindResponse(input);
            case "sort":
                return handleSortResponse(input);
            default:
                return UNKNOWN_COMMAND_MESSAGE;
        }
    }

    private String getTaskListResponse() {
        if (tasks.size() == 0) {
            return "Your task list is sparkling clean! ✨ No tasks to show right now.\n" +
                   "Ready to add some exciting goals? Just say the word! 🎯";
        }

        StringBuilder response = new StringBuilder();
        response.append("Behold! Your magnificent task collection: 📋✨\n");
        Task[] taskArray = tasks.getTaskArray();

        String taskList = IntStream.range(0, tasks.size())
                .mapToObj(i -> String.format("%d.%s", i + 1, taskArray[i]))
                .reduce((task1, task2) -> task1 + "\n" + task2)
                .orElse("");

        response.append(taskList);
        return response.toString();
    }

    private String handleMarkResponse(String input) throws BuddyException {
        int index = Parser.parseTaskNumber(input, "mark");
        Task task = tasks.getTask(index);
        tasks.markTask(index);
        saveTasksToFile();
        return "Woohoo! 🎉 Another victory achieved! I've marked this champion as DONE:\n  " +
               task + "\nYou're absolutely crushing it! Keep up the fantastic work! 💪";
    }

    private String handleUnmarkResponse(String input) throws BuddyException {
        int index = Parser.parseTaskNumber(input, "unmark");
        Task task = tasks.getTask(index);
        tasks.unmarkTask(index);
        saveTasksToFile();
        return "No worries! 🔄 Sometimes we need a do-over. I've unmarked this task:\n  " +
               task + "\nSecond chances are what make us stronger! You've got this! 💪";
    }

    private String handleDeleteResponse(String input) throws BuddyException {
        int index = Parser.parseTaskNumber(input, "delete");
        Task task = tasks.getTask(index);
        tasks.deleteTask(index);
        saveTasksToFile();
        return String.format("Poof! ✨ Task vanished into the digital void! Removed:\n  %s\n" +
                           "You now have %d tasks in your quest log. Stay focused, champion! 🎯",
                            task, tasks.size());
    }

    private String handleTodoResponse(String input) throws BuddyException {
        String description = Parser.parseDescription(input, "todo");
        Task task = new Todo(description);
        tasks.addTask(task);
        saveTasksToFile();
        return String.format("Excellent choice! 🌟 Your new adventure awaits:\n  %s\n" +
                           "Quest log updated! You now have %d tasks to conquer. Let's do this! 🚀",
                            task, tasks.size());
    }

    private String handleDeadlineResponse(String input) throws BuddyException {
        String[] parts = Parser.parseDeadline(input);
        Task task = new Deadline(parts[0], parts[1]);
        tasks.addTask(task);
        saveTasksToFile();
        return String.format("Time-sensitive mission accepted! ⏰ I've logged this deadline:\n  %s\n" +
                           "You've got %d tasks in your arsenal now. Beat that clock! ⚡",
                            task, tasks.size());
    }

    private String handleEventResponse(String input) throws BuddyException {
        String[] parts = Parser.parseEvent(input);
        Task task = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(task);
        saveTasksToFile();
        return String.format("Fantastic event scheduled! 🎪 Mark your calendar for:\n  %s\n" +
                           "Your event lineup now has %d items! Time to plan and shine! ✨",
                            task, tasks.size());
    }

    private String handleFindResponse(String input) throws BuddyException {
        String keyword = Parser.parseDescription(input, "find");
        Task[] matchingTasks = tasks.findTasks(keyword);

        if (matchingTasks.length == 0) {
            return "Hmm... 🕵️ No matching tasks found! Your search ninja skills are impressive, " +
                   "but this keyword is playing hide and seek! Try another term? 🔍";
        }

        String taskList = IntStream.range(0, matchingTasks.length)
                .mapToObj(i -> String.format("%d.%s", i + 1, matchingTasks[i]))
                .reduce((task1, task2) -> task1 + "\n" + task2)
                .orElse("");

        return "Detective work complete! 🕵️‍♂️ Found these matching treasures:\n" + taskList;
    }
    
    private String handleSortResponse(String input) throws BuddyException {
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2) {
            return "Hey there, organization master! 📊 Please tell me how to sort: " +
                   "'sort description', 'sort status', or 'sort type'. Let's get organized! 🗂️";
        }

        String sortBy = parts[1].toLowerCase();
        switch (sortBy) {
            case "description":
                tasks.sortByDescription();
                break;
            case "status":
                tasks.sortByStatus();
                break;
            case "type":
                tasks.sortByType();
                break;
            default:
                return "Oops! 🤷‍♀️ That's not a sorting method I know! Try: description, status, or type. " +
                       "I believe in your organizational skills! 💼";
        }

        saveTasksToFile();
        return String.format("Boom! 💥 Tasks perfectly organized by %s! Your list is now a masterpiece " +
                           "of efficiency! Marie Kondo would be proud! ✨", sortBy);
    }
    
    public static void main(String[] args) {
        new Buddy("./data/buddy.txt").run();
    }
}