package com.example.utils.ueditor.upload;

import com.example.utils.io.FileUtils;
import com.example.utils.lang.StringUtils;
import com.example.utils.media.VideoUtils;
import com.example.utils.ueditor.PathFormat;
import com.example.utils.ueditor.define.*;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
		FileItemStream fileStream = null; // 原始上传
		MultipartFile fileStream2 = null; // Spring MVC 上传
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;
				
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());

        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

		try {
			FileItemIterator iterator = upload.getItemIterator(request);

			while (iterator.hasNext()) {
				fileStream = iterator.next();

				if (!fileStream.isFormField()) {
					break;
				}
				fileStream = null;
			}

			if (fileStream == null) {
				// 原始上传无文件，则检查是否是Spring MVC上传 ThinkGem
				MultipartFile file = null;
				if (request instanceof MultipartHttpServletRequest){
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
					Iterator<String> it = multiRequest.getFileNames();
					while (it.hasNext()) {
						file = multiRequest.getFile(it.next());
						break;
					}
				}
				if (file != null && !file.isEmpty() && file.getOriginalFilename() != null) {
					fileStream2 = file;
				}
			}
			
			if (fileStream == null && fileStream2 == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("savePath");
			String originFileName = fileStream != null ? fileStream.getName() : fileStream2.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

			String physicalPath = FileUtils.path((String) conf.get("rootPath") + savePath);

			InputStream is = fileStream != null ? fileStream.openStream() : fileStream2.getInputStream();
			State storageState = StorageManager.saveFileByInputStream(is, physicalPath, maxSize);
			is.close();

			if (storageState.isSuccess()) {
				int actionCode = ((Integer) conf.get("actionCode")).intValue();
				String ctx = request.getContextPath(); // ThinkGem 修正上传图片后返回无contextpath问题
				
				// 上传图片后，进行图片压缩
				if (actionCode == ActionMap.UPLOAD_IMAGE){
					
					// 如果开启了压缩图片
					if ((Boolean)conf.get("imageCompressEnable")){
						Integer width = (Integer)conf.get("imageCompressBorder");
						// 过滤掉gif图片，因为gif图片转换后会出现黑色背景的情况（gif基本也没有比较大的图片）。
						if (StringUtils.inString(FileUtils.getFileExtension(physicalPath),
								"png","jpg","jpeg","bmp","ico")){
							BufferedImage bufferedImage = ImageIO.read(new File(physicalPath));
							Builder<BufferedImage> file = Thumbnails.of(bufferedImage);
							if (bufferedImage != null){
								if (bufferedImage.getWidth() <= width){
									file.width(bufferedImage.getWidth());
								}else{
									file.width(width);
								}
								file.toFile(physicalPath);
							}
						}
					}
					
				}
				
				// 上传成功后 转换格式 按照新的视频格式 返回前台 ThinkGem
				else if(actionCode == ActionMap.UPLOAD_VIDEO){
					final VideoUtils v = new VideoUtils(physicalPath);
					// 先截图 
					if (v.cutPic()){
						// 开启进程，在转换视频文件
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(5000);
									v.convert();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}).start();  
						storageState.putInfo("url", ctx + PathFormat.format(savePath) + "." + v.getOutputFileExtension());
						storageState.putInfo("type", "." + v.getOutputFileExtension());
						storageState.putInfo("original", originFileName +"."+ v.getInputFileExtension());
						return storageState;
					}
				}
				storageState.putInfo("url", ctx + PathFormat.format(savePath));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}

			return storageState;
		} catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
	
}
