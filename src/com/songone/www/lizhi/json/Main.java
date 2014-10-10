/**
 * 
 */
package com.songone.www.lizhi.json;

import java.util.HashMap;
import java.util.Map;

import com.songone.www.base.utils.ConsoleInputUtil;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.lizhi.json.start.GetAndSaveRadioAudioDatasMain;
import com.songone.www.lizhi.json.start.GetAndSaveRadioDatasMain;
import com.songone.www.lizhi.json.start.SyncRadioAudioDatasMain;
import com.songone.www.lizhi.json.start.SyncRadioDatasMain;
import com.songone.www.lizhi.json.start.UploadToAliyunDataModelMain;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class Main {
	// // start
	public static void main(String[] args) {
		// // 初始加载
		SystemUtil.init();
		menu();
		// // 退出
		// SystemUtil.exit(0);
	}

	public static final Map<Integer, String> menu_map = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 7029514270377828215L;
		{
			put(1, "获取荔枝FM的电台数据并保存到数据库");
			put(2, "获取荔枝FM的电台节目数据并保存到数据库");
			put(3, "同步荔枝FM的电台数据到音乐1号服务器");
			put(4, "同步荔枝FM的电台节目数据到音乐1号服务器");
			put(5, "同步荔枝FM的文件数据到阿里云服务器");
			put(0, "退出");
		}

	};

	public static void menu() {
		System.out.println();
		System.out.println("======================= 功能选择 =======================");
		for (Map.Entry<Integer, String> entry : menu_map.entrySet()) {
			System.out.println(" " + entry.getKey() + " : " + entry.getValue());
		}
		int key = ConsoleInputUtil.getInt("请选择操作项 : ");
		switch (key) {
		case 1:
			GetAndSaveRadioDatasMain.getAndSaveRadioDatasStart();
			break;
		case 2:
			GetAndSaveRadioAudioDatasMain.getAndSaveRadioAudioDatasStart();
			break;
		case 3:
			SyncRadioDatasMain.syncRadioDatasStart();
			break;
		case 4:
			SyncRadioAudioDatasMain.syncAudioDatasStart();
			break;
		case 5:
			UploadToAliyunDataModelMain.uploadDatas();
			break;
		case 0:
			System.exit(0);
			break;
		default:
			System.exit(0);
			break;
		}
	}

}
