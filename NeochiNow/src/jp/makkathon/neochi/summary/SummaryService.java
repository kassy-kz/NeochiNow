package jp.makkathon.neochi.summary;

import java.util.Date;

/**
 * 寝落ち集計インタフェース
 * 
 * @author kuchitama
 */
public interface SummaryService {
	
	/**
	 * 寝落ちした日を登録する
	 * @param date
	 */
	void registerNeochiDate(Date date);
	
	/**
	 * 月間寝落ち回数を取得する
	 * 
	 * @param targetMonth 対象月（yyyy/MM)
	 * @return 対象月の月間寝落ち回数
	 */
	int getMonthlyCount(String targetMonth);
}
