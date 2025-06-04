package library;

public class Publication extends BookItem {
	
	public Publication(String id, String title, String author, String type, String status, String journalConference) {
		super(id, title, author, type, status, journalConference); // journalConference is passed as source
	}

	public String getJournalConference() {
		return getSource(); // source from BookItem
	}

}
