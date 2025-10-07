package Max.model;

public class PlayList{
    private class PlayListItem<Song>{
        public Song song;
        public PlayListItem<Song> next;
        public PlayListItem(Song song, PlayListItem<Song> next){
            this.song = song;
            this.next = next;
        }
    }
}
