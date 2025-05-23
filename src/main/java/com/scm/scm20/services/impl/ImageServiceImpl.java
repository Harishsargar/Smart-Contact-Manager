package com.scm.scm20.services.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.scm20.helper.AppConstant;
import com.scm.scm20.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;
    
  

    @Override
    public String uploadImage(MultipartFile contactImage, String filename) {

        
        // String filename = UUID.randomUUID().toString();
        try {
            byte[] data = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);
            // cloudinary upload
            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id", filename));
            System.out.println(" Image Upload : " + filename);
            return this.getUrlFromPublicId(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        
    }



    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary.url().transformation(
            new Transformation<>().width(AppConstant.CONTACT_IMAGE_WIDTH).height(AppConstant.CONTACT_IMAGE_HEIGHT).crop(AppConstant.CONTACT_IMAGE_CROP)
        ).generate(publicId);
        
    }



    @Override
    public String deleteImage(String cloudinaryImageIdicId) {
        try {
            cloudinary.uploader().destroy(cloudinaryImageIdicId, ObjectUtils.emptyMap());
            return "Image deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting image";
        }
    }

}
