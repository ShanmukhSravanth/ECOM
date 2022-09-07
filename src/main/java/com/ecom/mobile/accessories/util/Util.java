package com.ecom.mobile.accessories.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Util {

	static SimpleDateFormat sf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");

	private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[!@$%&*-_.]).{8,40})";

	public static boolean validatePass(final String password) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	/**
	 * <b> Eg:</b> getDateByString("31/01/2019", "dd/MM/yyyy")
	 * 
	 * @return Date
	 */
	public static Date getDateByString(String str, String format) {
		try {
			return new SimpleDateFormat(format).parse(str);
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * <b> Eg:</b> getDateStringByString("31/01/2019 23:10:11", "dd/MM/yyyy
	 * HH:mm:ss", "yyyy-MM-dd HH:mm:ss")
	 * 
	 * @return String
	 */
	public static String getDateStringByString(String str, String srcFormat, String dstFormat) {
		try {
			Date date = getDateByString(str, srcFormat);
			DateFormat dateFormat = new SimpleDateFormat(dstFormat);
			return dateFormat.format(date);
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * <b> Eg:</b> getStringByDate(date, format)
	 * 
	 * @return String
	 */
	public static String getStringByDate(Date date, String format) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * <b> Eg:</b> daysBetween(maxDate, minDate)
	 * 
	 * @return long
	 */
	public static long daysBetween(Date maxDate, Date minDate) {
		long difference = (maxDate.getTime() - minDate.getTime()) / 86400000;
		return Math.abs(difference);
	}

	/**
	 * <b> Eg:</b> subtractDays(srcDate, 2)
	 * 
	 * @return Date
	 */
	public static Date subtractDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, -days);
		return cal.getTime();
	}

	/**
	 * <b> Eg:</b> fromTo(startDate, endDate) <br>
	 * <br>
	 * startDate, endDate format= yyyy-MM-dd
	 * 
	 * @return Date[]
	 */
	public static Date[] fromTo(Date startDate, Date endDate) {
		String fromDateStr = sf2.format(startDate);
		String toDateStr = sf2.format(startDate);
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = sf3.parse(fromDateStr + " 00:00:00");
			toDate = sf3.parse(toDateStr + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Date[] { fromDate, toDate };
	}
	////////// Files //////////////////////////

	public static void createFolder(String path) {
		File dir = new File(path);
		// create output directory if it doesn't exist
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static String itarateAndZipFiles(String source, String fileName) {
		ArrayList<String> ar = new ArrayList<String>();
		File upload;
		File[] listOfFiles;
		listOfFiles = getFiles(source);
		if (listOfFiles != null && listOfFiles.length != 0)
			for (int i = 0; i < listOfFiles.length; i++) {
				upload = listOfFiles[i];
				if (upload != null) {
					ar.add(source + "/" + upload.getName());
				}
			}
		return zipFiles(source, ar, fileName);
	}

	public static String zipFiles(String source, ArrayList<String> srcFiles, String fileName) {
		fileName = source + fileName;
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			ZipOutputStream zipOut = new ZipOutputStream(fos);
			for (String srcFile : srcFiles) {
				File fileToZip = new File(srcFile);
				FileInputStream fis = new FileInputStream(fileToZip);
				ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
				zipOut.putNextEntry(zipEntry);

				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
				fis.close();
			}
			zipOut.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	private static File[] getFiles(String source) {
		File folder = new File(source);
		File[] listOfFiles = folder.listFiles();
//		for (File file : listOfFiles) {
//			System.out.println(file.getName());
//		}
		return listOfFiles;
	}

	////////////////////////// Zip Folder//////////////////////

	/**
	 * This method zips the directory
	 * 
	 * @param dir
	 * @param zipDirName
	 */
	public static void zipDirectory(File dir, String zipDirName) {
		List<String> filesListInDir = new ArrayList<String>();
		try {
			populateFilesList(dir, filesListInDir);
			// now zip files one by one
			// create ZipOutputStream to write to the zip file
			FileOutputStream fos = new FileOutputStream(zipDirName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String filePath : filesListInDir) {
				System.out.println("Zipping " + filePath);
				// for ZipEntry we need to keep only relative file path, so we used substring on
				// absolute path
				ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
				zos.putNextEntry(ze);
				// read the file and write to ZipOutputStream
				FileInputStream fis = new FileInputStream(filePath);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
				fis.close();
			}
			zos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method populates all the files in a directory to a List
	 * 
	 * @param dir
	 * @throws IOException
	 */
	private static void populateFilesList(File dir, List<String> filesListInDir) throws IOException {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile())
				filesListInDir.add(file.getAbsolutePath());
			else
				populateFilesList(file, filesListInDir);
		}
	}

	////////// Data Types//////////////////////

	/**
	 * <b> Eg:</b> compareBigDecimal(bigDecimalOne, bigDecimalTwo)
	 * 
	 * @return int <br>
	 *         bigDecimalOne > bigDecimalTwo result 1 <br>
	 *         bigDecimalOne < bigDecimalTwo result -1 <br>
	 *         bigDecimalOne == bigDecimalTwo result 0
	 */
	public static int compareBigDecimal(BigDecimal bigDecimalOne, BigDecimal bigDecimalTwo) {
		return bigDecimalOne.compareTo(bigDecimalTwo);
	}

	/**
	 * <b> Eg:</b> getSpacifedDecimals(doubleVal, numOfDeciVal)
	 * 
	 * @return String <br>
	 *         Get requested number of Decimal values After dot <br>
	 * 
	 *         numOfDeciVal ---- ".00" for 2 digits <br>
	 *         numOfDeciVal ---- ".000" for 3 digits <br>
	 * 
	 */
	public static String getSpacifedDecimals(double doubleVal, String numOfDeciVal) {
		DecimalFormat formatter = new DecimalFormat(numOfDeciVal);
		return formatter.format(doubleVal);
	}

	public static int getRandomIntInRange(int range) {
		return new SecureRandom().nextInt(range);
	}

	public static Date getDateTime(String expiry) {
		// 12-7-2016

		try {
			if (expiry != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				if (expiry.matches("[0-9]*[-][0-9]*[-][0-9]*[ ][0-9]*[:][0-9]*[:][0-9]*")) {
					return sdf.parse(expiry);
				} else if (expiry.matches("[0-9]*[-][0-9]*[-][0-9]*[ ][0-9]*[:][0-9]*")) {
					expiry += ":00";
					return sdf.parse(expiry);
				} else if (expiry.matches("[0-9]*[-][0-9]*[-][0-9]*")) {
					expiry += " " + getTime();// 23:59:59
					return sdf.parse(expiry);
				} else if (expiry.matches("[0-9]*")) {
					return sdf.parse(expiry);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Date();// default value
	}

	public static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//23:59:59
		return dateFormat.format(new Date());
	}

	public static boolean isValidInet4Address(String ip) {

		String IPv4_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
				+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

		Pattern IPv4_PATTERN = Pattern.compile(IPv4_REGEX);

		if (ip == null) {
			return false;
		}

		Matcher matcher = IPv4_PATTERN.matcher(ip);

		return matcher.matches();
	}

	public static void main(String[] args) {
		// System.out.println(getDateByString("31/12/1998", "dd/MM/yyyy"));

		// System.out.println(daysBetween(new Date(), getDateByString("01/07/2019",
		// "dd/MM/yyyy")));

		// System.out.println(getDateStringByString("31/12/1998 23:10:11", "dd/MM/yyyy
		// HH:mm:ss", "yyyy-MM-dd HH:mm:ss"));

		// System.out.println(compareBigDecimal(new BigDecimal("10000"), new
		// BigDecimal("10000")));

		// System.out.println(getSpacifedDecimals("98237498.2804"));

		// System.out.println(getDateTime("07/OCT/2019 23:20:10"));

//		String sha1=editArray1("101010","NewRecord",8);
//		String sha2=editArray1("101010,999999","NewRecord",8);
//		String sha3=editArray1("101010,999999,888888","NewRecord",8);
//		String sha4=editArray1("101010,999999,888888,777777","NewRecord",8);
//		String sha5=editArray1("101010,999999,888888,777777,666666","NewRecord",8);
//		String sha6=editArray1("101010,999999,888888,777777,666666,555555","NewRecord",8);
//		String sha7=editArray1("101010,999999,888888,777777,666666,555555,444444","NewRecord",8);
//		String sha8=editArray1("101010,999999,888888,777777,666666,555555,444444,333333","NewRecord",8);
//		String sha9=editArray1("101010,999999,888888,777777,666666,555555,444444,333333,222222","NewRecord",9);
//		String sha10=editArray1("101010,999999,888888,777777,666666,555555,444444,333333,222222,111111","NewRecord",10);
//		String sha11=editArray1("101010,999999,888888,777777,666666,555555,444444,333333,222222,111111,000000","NewRecord",11);
//		
//		System.out.println(sha1);
//		System.out.println(sha2);
//		System.out.println(sha3);
//		System.out.println(sha4);
//		System.out.println(sha5);
//		System.out.println(sha6);
//		System.out.println(sha7);
//		System.out.println(sha8);
//		System.out.println(sha9);
//		System.out.println(sha10);
//		System.out.println(sha11);

		Date d1 = getDateByString("08-07-2019", "dd-MM-yyyy");

		System.out.println(d1);

		Date d2 = getDateByString("09-07-2019", "dd-MM-yyyy");

		System.out.println(d2);

		System.out.println(d1.compareTo(d2));

	}

	public static String editArray(String array, String value) {
		String[] newStringArr = null;
		int size = 0;
		if (array != null) {
			newStringArr = array.split(",");
			size = newStringArr.length;
		}
		String[] newArray = new String[9];
		if (size > 8) {
			int j = 8;
			for (int i = 0; i <= 7; i++) {
				newArray[i] = newStringArr[size - j];
				j--;
			}
			newArray[8] = value;
		} else if (array != null) {
			return array + "," + value;
		} else {
			return value;
		}
		return arrayToString(newArray, ",");
	}

	/**
	 * <b> EG:</b> arrayToString(arrayValue, ",")
	 * 
	 * @return String <br>
	 *         Get String from array data with required supporator like ',' '|'..etc
	 *         <br>
	 * 
	 */
	private static String arrayToString(String[] arrayData, String supporator) {
		String str = null;
		for (int i = 0; i < arrayData.length; i++) {
			if (str != null) {
				str += supporator + arrayData[i];
			} else {
				str = arrayData[i];
			}
		}
		return str;
	}

	public static String editArray1(String array, String value, int arraySize) {
		String[] newStringArr = array.split(",");
		int size = newStringArr.length;

		if (size < arraySize)
			return value + "," + array;
		String[] newArray = new String[arraySize + 1];
		newArray[0] = value;
		for (int i = 0; i < arraySize; i++) {
			newArray[i + 1] = newStringArr[i];
		}
		return arrayToString(newArray, ",");
	}

	public static void displayException(Exception e) {
		e.printStackTrace();
	}

	public static boolean isEmpty(String val) {
		return val == null || val.trim().length() == 0;
	}

	public static String getOrderID() {
		String stan = getStan();
		stan = zeroPad(stan, 6);
		Date d = new Date();
		String time = getDateTime(d, TimeZone.getTimeZone("IST"));
		String jd = getJulianDate(d);
		return jd + time.substring(4, 6) + zeroPad(stan + "", 6);
	}
	
	public static String getStan() {
		return String.valueOf(System.currentTimeMillis() % 1000000);
	}

	public static String getJulianDate(Date d) {
		String day = formatDate(d, "DDD", TimeZone.getDefault());
		String year = formatDate(d, "yy", TimeZone.getDefault());
		year = year.substring(1);
		return year + day;
	}

	public static String getDateTime(Date d, TimeZone timeZone) {
		return formatDate(d, "MMddHHmmss", timeZone);
	}

	public static String formatDate(Date d, String pattern, TimeZone timeZone) {
		SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateTimeInstance();
		df.setTimeZone(timeZone);
		df.applyPattern(pattern);
		return df.format(d);
	}

	public static String zeroPad(String s, int len) {
		StringBuilder builder = new StringBuilder(s);
		while (builder.length() < len) {
			builder.insert(0, '0');
		}
		return builder.toString();
	}
}