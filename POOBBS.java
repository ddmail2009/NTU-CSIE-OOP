public class POOBBS{
	public static void main(String[] args) {
		POODirectory dir = new POODirectory("favorite");
		dir.show();

		dir.add(new POOBoard("Gossip"));
		dir.add(new POOBoard("Girl"));
		dir.show();


		POOBoard tmp = new POOBoard("Test");
		tmp.add(new POOArticle("test", "wang", "haha"));
		dir.add(tmp);
		dir.show();

		dir.add_split();
		dir.show();

		dir.add( new POODirectory("NTU"));
		dir.show();
	}
}