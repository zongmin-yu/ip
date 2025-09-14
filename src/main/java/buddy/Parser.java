package buddy;

public class Parser {
    private static final int DEADLINE_COMMAND_LENGTH = 8;  // "deadline".length()
    private static final int EVENT_COMMAND_LENGTH = 5;     // "event".length()
    private static final int TASK_NUMBER_BASE = 1; // Tasks are 1-indexed for users
    
    public static String parseCommand(String input) throws BuddyException {
        if (input == null) {
            throw new BuddyException("🚨 Oops! I received an empty message! Please tell me what you'd like to do! 💬");
        }

        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new BuddyException("🤔 Hmm, that's a very quiet command! Could you please type something? I'm all ears! 👂");
        }

        // Check for whitespace-only input or unusual characters
        if (!trimmedInput.matches(".*[a-zA-Z0-9].*")) {
            throw new BuddyException("🎯 I need some actual letters or numbers to understand you! Try commands like 'list' or 'todo'! ✨");
        }

        String[] words = trimmedInput.split("\\s+");
        return words.length > 0 ? words[0].toLowerCase() : "";
    }
    
    public static String parseDescription(String input, String command) throws BuddyException {
        if (input == null || command == null) {
            throw new BuddyException("🚨 Internal error! Missing input or command information!");
        }

        if (input.length() < command.length()) {
            throw new BuddyException("🤖 Hmm, looks like you might have typed an incomplete command! Try typing the full command first! 💡");
        }

        String description = input.substring(command.length()).trim();

        if (description.isEmpty()) {
            String suggestions = getDescriptionSuggestions(command);
            throw new BuddyException(String.format("🎯 A %s needs a description! %s", command, suggestions));
        }

        // Check for excessively long descriptions
        if (description.length() > 200) {
            throw new BuddyException("📝 Wow, that's a novel! Please keep descriptions under 200 characters for better organization! ✂️");
        }

        // Check for suspicious patterns
        if (description.matches("^[\\s\\p{P}]+$")) {
            throw new BuddyException("🔤 I need some actual words in that description! Please add some meaningful content! 📖");
        }

        return description;
    }

    private static String getDescriptionSuggestions(String command) {
        switch (command) {
            case "todo":
                return "Try: 'todo buy groceries' 🛒";
            case "find":
                return "Try: 'find homework' 🔍";
            default:
                return "Please add some details after the command! ✨";
        }
    }
    
    public static String[] parseDeadline(String input) throws BuddyException {
        if (input.length() <= DEADLINE_COMMAND_LENGTH) {
            throw new BuddyException("⏰ A deadline needs both a task description and a deadline! " +
                                   "Try: 'deadline submit report /by 2024-12-01' 📝");
        }

        String remaining = input.substring(DEADLINE_COMMAND_LENGTH).trim();
        String[] parts = remaining.split(" /by ");

        if (parts.length < 2) {
            throw new BuddyException("🕒 Missing the '/by' part! Format: 'deadline <task> /by <date>'\n" +
                                   "Example: 'deadline finish homework /by tomorrow 2pm' ⏰");
        }

        String description = parts[0].trim();
        String deadline = parts[1].trim();

        if (description.isEmpty()) {
            throw new BuddyException("📋 What task has this deadline? Please add a description before '/by'! 💭");
        }

        if (deadline.isEmpty()) {
            throw new BuddyException("📅 When is this due? Please add a date/time after '/by'! " +
                                   "Examples: '2024-12-01', 'tomorrow', 'next Friday' ⏰");
        }

        // Check for overly long descriptions or dates
        if (description.length() > 150) {
            throw new BuddyException("📝 That description is quite detailed! Please keep it under 150 characters! ✂️");
        }

        if (deadline.length() > 100) {
            throw new BuddyException("📅 That's a very specific deadline! Please keep date info under 100 characters! ⏰");
        }

        return new String[]{description, deadline};
    }
    
    public static String[] parseEvent(String input) throws BuddyException {
        if (input.length() <= EVENT_COMMAND_LENGTH) {
            throw new BuddyException("🎪 An event needs a description and timing! " +
                                   "Try: 'event meeting /from 2pm /to 4pm' 🗓️");
        }

        String remaining = input.substring(EVENT_COMMAND_LENGTH).trim();
        String[] parts = remaining.split(" /from ");

        if (parts.length < 2) {
            throw new BuddyException("🕒 Missing the '/from' part! Format: 'event <description> /from <start> /to <end>'\n" +
                                   "Example: 'event team meeting /from 2pm /to 4pm' 📅");
        }

        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new BuddyException("🎯 What's this event about? Please add a description before '/from'! 💭");
        }

        String[] timeParts = parts[1].split(" /to ");
        if (timeParts.length < 2) {
            throw new BuddyException("⏰ Missing the '/to' part! I need both start and end times!\n" +
                                   "Format: 'event <description> /from <start> /to <end>' 🕰️");
        }

        String startTime = timeParts[0].trim();
        String endTime = timeParts[1].trim();

        if (startTime.isEmpty()) {
            throw new BuddyException("🕐 When does this event start? Please specify a start time after '/from'! " +
                                   "Examples: '9am', '14:00', 'Monday 2pm' ⏰");
        }

        if (endTime.isEmpty()) {
            throw new BuddyException("🕐 When does this event end? Please specify an end time after '/to'! " +
                                   "Examples: '5pm', '17:00', 'Monday 4pm' ⏰");
        }

        // Validate lengths
        if (description.length() > 150) {
            throw new BuddyException("📝 That's quite an event description! Please keep it under 150 characters! ✂️");
        }

        if (startTime.length() > 50) {
            throw new BuddyException("🕒 That start time is very detailed! Please keep it under 50 characters! ⏰");
        }

        if (endTime.length() > 50) {
            throw new BuddyException("🕒 That end time is very detailed! Please keep it under 50 characters! ⏰");
        }

        return new String[]{description, startTime, endTime};
    }
    
    public static int parseTaskNumber(String input, String command) throws BuddyException {
        if (input == null || command == null) {
            throw new BuddyException("🚨 Internal error! Missing command information!");
        }

        if (input.length() <= command.length()) {
            throw new BuddyException(String.format("🔢 Which task? Please add a task number after '%s'! " +
                                   "Example: '%s 1' 📋", command, command));
        }

        String numberStr = input.substring(command.length()).trim();

        if (numberStr.isEmpty()) {
            throw new BuddyException(String.format("🎯 I need a task number! Try '%s 1', '%s 2', etc. 🔢", command, command));
        }

        // Check for multiple numbers or extra text
        String[] parts = numberStr.split("\\s+");
        if (parts.length > 1) {
            throw new BuddyException("🤔 I can only handle one task at a time! Please use just one number. " +
                                   "Example: '" + command + " 3' 🎯");
        }

        try {
            int taskNumber = Integer.parseInt(numberStr);

            if (taskNumber <= 0) {
                throw new BuddyException("📊 Task numbers start from 1, not " + taskNumber + "! " +
                                       "Please use positive numbers like 1, 2, 3... 🔢");
            }

            if (taskNumber > 10000) {
                throw new BuddyException("🚀 Whoa! That's task number " + taskNumber + "! " +
                                       "Are you sure you have that many tasks? Please double-check! 📋");
            }

            return taskNumber - TASK_NUMBER_BASE; // Convert to 0-based index

        } catch (NumberFormatException e) {
            if (numberStr.contains(".") || numberStr.contains(",")) {
                throw new BuddyException("🔢 Please use whole numbers only! Try '3' instead of '3.5' or '3,0' 📊");
            } else {
                throw new BuddyException("🤖 '" + numberStr + "' doesn't look like a number to me! " +
                                       "Please use digits like 1, 2, 3... 🔢");
            }
        }
    }
}