package buddy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    
    @Test
    void parseCommand_validCommands_success() throws BuddyException {
        assertEquals("todo", Parser.parseCommand("todo read book"));
        assertEquals("deadline", Parser.parseCommand("deadline submit report /by Sunday"));
        assertEquals("list", Parser.parseCommand("list"));
        assertEquals("bye", Parser.parseCommand("bye"));
        assertEquals("mark", Parser.parseCommand("MARK 1"));
    }

    @Test
    void parseCommand_emptyInput_throwsException() {
        assertThrows(BuddyException.class, () -> Parser.parseCommand(""));
        assertThrows(BuddyException.class, () -> Parser.parseCommand("   "));
    }
    
    @Test
    void parseDeadline_validFormat_success() throws BuddyException {
        String[] result = Parser.parseDeadline("deadline return book /by Sunday");
        assertEquals("return book", result[0]);
        assertEquals("Sunday", result[1]);
        
        String[] result2 = Parser.parseDeadline("deadline submit assignment /by 2024-12-25");
        assertEquals("submit assignment", result2[0]);
        assertEquals("2024-12-25", result2[1]);
    }
    
    @Test
    void parseDeadline_invalidFormat_throwsException() {
        assertThrows(BuddyException.class, () -> Parser.parseDeadline("deadline return book"));
        assertThrows(BuddyException.class, () -> Parser.parseDeadline("deadline /by Sunday"));
        assertThrows(BuddyException.class, () -> Parser.parseDeadline("deadline return book /by "));
    }
    
    @Test
    void parseTaskNumber_validNumber_success() throws BuddyException {
        assertEquals(0, Parser.parseTaskNumber("mark 1", "mark"));
        assertEquals(4, Parser.parseTaskNumber("delete 5", "delete"));
        assertEquals(9, Parser.parseTaskNumber("unmark 10", "unmark"));
    }
    
    @Test
    void parseTaskNumber_invalidNumber_throwsException() {
        assertThrows(BuddyException.class, () -> Parser.parseTaskNumber("mark abc", "mark"));
        assertThrows(BuddyException.class, () -> Parser.parseTaskNumber("delete ", "delete"));
    }
}