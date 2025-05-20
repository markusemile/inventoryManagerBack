package com.markdev.inventoryManagmentsSystem.service.impl;

import com.markdev.inventoryManagmentsSystem.dto.ProductDto;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.entity.Category;
import com.markdev.inventoryManagmentsSystem.entity.Product;
import com.markdev.inventoryManagmentsSystem.exceptions.NotFoundException;
import com.markdev.inventoryManagmentsSystem.repository.CategoryRepository;
import com.markdev.inventoryManagmentsSystem.repository.ProductRepository;
import com.markdev.inventoryManagmentsSystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private  final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image";
    private  final String THUMB_DIRECTORY = System.getProperty("user.dir") + "/product-image/thumbnails";

    @Override
    public Response createProduct(ProductDto productDto, MultipartFile imageFile) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(()->new NotFoundException("This category id :"+productDto.getCategoryId()+" not found!"));

        Product  productToSave = Product.builder()
                .name(productDto.getName())
                .sku(productDto.getSku())
                .price(productDto.getPrice())
                .stockQuantity(productDto.getStockQuantity())
                .description((productDto.getDescription()))
                .category(category)
                .build();

        if (imageFile != null) {

            String imagePath = saveImage(productToSave.getSku(),imageFile);
            productToSave.setImageUrl(productToSave.getSku());

        }

        productToSave.setCategory(category);

        productRepository.save(productToSave);

        return Response.builder()
                .status(200)
                .message("Product successfully created.")
                .build();
    }

    @Override
    public Response getAllProducts(Pageable pageable) {
        Page<Product> pageableProducts = productRepository.findAll(pageable);
        List<ProductDto> allDtoProducts = modelMapper.map(pageableProducts.getContent(), new TypeToken<List<ProductDto>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(allDtoProducts)
                .totalElement(pageableProducts.getTotalElements())
                .totalElement(pageableProducts.getTotalElements())
                .currentPage(pageableProducts.getNumber())
                .build();
    }

    @Override
    public Response getProductById(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()->new NotFoundException("This product id :"+id+" not found !"));
        ProductDto productDto = modelMapper.map(existingProduct,ProductDto.class);

        return Response.builder()
                .status(200)
                .message("success")
                .product(productDto)
                .build();
    }

    @Override
    public Response updateProduct(ProductDto productDto,MultipartFile filename) {



        Product existingProduct = productRepository.findById(productDto.getId())
                .orElseThrow(()->new NotFoundException("Product id:"+productDto.getId()+" not found"));

        // check if image is associate to the updating
        if(filename != null && !filename.isEmpty()){
            String imagePath = saveImage(productDto.getSku(),filename);
            final String extension = getFileExtension(filename);
            existingProduct.setImageUrl(productDto.getSku()+"."+extension);
        }
        // check if the category is to be changed
        if(productDto.getId() != null && productDto.getCategoryId()>0 && productDto.getCategoryId() != existingProduct.getCategory().getId()){
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(()->new NotFoundException("Category with id:"+productDto.getCategoryId()+" not found"));
        }

        // check and update file
        if(productDto.getName()!=null && !productDto.getName().isBlank()) existingProduct.setName(productDto.getName());
        if(productDto.getSku() !=null && !productDto.getSku().isBlank()) existingProduct.setSku(productDto.getSku());
        if(productDto.getDescription() !=null && !productDto.getDescription().isBlank()) existingProduct.setDescription(productDto.getDescription());
        if(productDto.getPrice() !=null && productDto.getPrice().compareTo(BigDecimal.ZERO) >=0) existingProduct.setPrice(productDto.getPrice());
        if(productDto.getStockQuantity() != null && productDto.getStockQuantity()>=0) existingProduct.setStockQuantity(productDto.getStockQuantity());

        existingProduct.setUpdateAt(LocalDateTime.now());

        productRepository.save(existingProduct);

        return Response.builder()
                .status(200)
                .message("Product successfully updated")
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {

       productRepository.findById(id)
                .orElseThrow(()->new NotFoundException("This product id :"+id+" not found !"));

        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product successfully deleted")
                .build();

    }

    @Override
    public Response searchProductPaginated(String searchText, Pageable pageable) {
        Page<Product> pagedProducts;

        if(searchText == null || searchText.trim().isEmpty()){
            pagedProducts = this.productRepository.findAll(pageable);
        }else{
            pagedProducts = this.productRepository.searchProductByWord(searchText, pageable);
        }

        List<ProductDto> dtoList = modelMapper.map(pagedProducts.getContent(), new TypeToken<List<ProductDto>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(dtoList)
                .totalPages(pagedProducts.getTotalPages())
                .currentPage(pagedProducts.getNumber())
                .totalElement(pagedProducts.getTotalElements())
                .build();

    }

    private String saveImage(String sku,MultipartFile imageToSave){

        // check type
        if(!imageToSave.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only images files are allowed");
        }

        // create folders if it doesn't exist
        File directory = new File(IMAGE_DIRECTORY);
        File thumbnail = new File(THUMB_DIRECTORY);

        if(!directory.exists()){
            directory.mkdir();
        }if(!thumbnail.exists()){
            thumbnail.mkdir();
        }

        final String extension = getFileExtension(imageToSave);
        String writeFormat = extension.equalsIgnoreCase("png") ? "png" : "jpg";

        // generate uniq name file
        String uniqFilename = sku.trim().replaceAll(" ","")+"."+extension;
        String uniqThumbFilename = sku.trim().replaceAll(" ","")+"_thumb."+writeFormat;

        // get absolute path of the image
        String imagePath = IMAGE_DIRECTORY + "/" + uniqFilename;
        String thumbPath = THUMB_DIRECTORY + "/" + uniqThumbFilename;

        try{
            // saving original size

            File destinantionFile = new File(imagePath);
            File destinationThumb = new File(thumbPath);

            // thumbnail creation
            BufferedImage originalImage = ImageIO.read(imageToSave.getInputStream());

            if (originalImage == null) {
                throw new IllegalArgumentException("Unsupported or corrupted image file.");
            }


            // Dimensions thumbnail
            int targetWidth = 200;
            int targetHeight = 300;

            // make the background
            BufferedImage thumbnailImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = thumbnailImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, targetWidth, targetHeight);

            // dimension calc
            double scale = Math.max((double) targetWidth / originalImage.getWidth(), (double) targetHeight / originalImage.getHeight());
            int newWidth = (int) (originalImage.getWidth() * scale);
            int newHeight = (int) (originalImage.getHeight() * scale);

            // centered the picture
            int x = (targetWidth - newWidth) / 2;
            int y = (targetHeight - newHeight) / 2;

            // draw the new image
            g.drawImage(originalImage, x, y, newWidth, newHeight, null);
            g.dispose();



            // write the thumbnail
            ImageIO.write(thumbnailImage, writeFormat, destinationThumb);

            // write original picture
            imageToSave.transferTo(destinantionFile);

        }catch(Exception e){
                throw new IllegalArgumentException(("Error occurred white saving image.\n"+e.getMessage()));
        }
        return imagePath;

    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        } else {
            return null;
        }
    }
}
