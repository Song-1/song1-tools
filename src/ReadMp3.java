import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.upload.aliyun.util.FileDoUtil;

public class ReadMp3 {
	private RandomAccessFile ran = null;
	private File file = null;

	public ReadMp3() throws FileNotFoundException, URISyntaxException, MalformedURLException {
		file = new File("//testupload2.oss-cn-hangzhou.aliyuncs.com/cherrytimemusic/qq/song.mp3");
		ran = new RandomAccessFile(file, "r");
		FileDoUtil.outLog("文件裝載完畢");

	}

	public static void main(String[] args) throws Exception {
		ReadMp3 read = new ReadMp3();
		byte[] buffer = new byte[128];

		read.ran.seek(read.ran.length() - 128);

		read.ran.read(buffer);
		SongInfo info = new SongInfo(buffer);
		FileDoUtil.outLog("name:" + info.getSongName() + " year:" + info.getYear() + " 歌手:" + info.getArtist() + " 專輯名:" + info.getAlbum() + " 備注:" + info.getComment());

	}
}
