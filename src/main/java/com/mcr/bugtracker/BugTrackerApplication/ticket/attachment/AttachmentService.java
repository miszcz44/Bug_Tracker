package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import com.mcr.bugtracker.BugTrackerApplication.GCS.DataBucketUtil;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.file.FileDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AttachmentService {

    private final TicketService ticketService;
    private final DataBucketUtil dataBucketUtil;

    private final AttachmentRepository attachmentRepository;
    public Attachment saveAttachment(AttachmentRequest request) {
        //Attachment attachment = new Attachment(request.getFile(),
                                               // request.getNotes());
        //attachment.setTicket(ticketService.findById(request.getTicketId()).orElseThrow());
        Attachment attachment = new Attachment();

        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getAllAttachmentsByTicketId(Long ticketId) {
        return attachmentRepository.findAllAttachmentsByTicketId(ticketId);
    }

    public List<File> uploadFilesToGCS(MultipartFile[] files) {
        List<File> inputFiles = new ArrayList<>();

        Arrays.asList(files).forEach(file -> {
            String originalFileName = file.getOriginalFilename();
            if(originalFileName == null){
                //throw new BadRequestException("Original file name is null");
            }
            Path path = new File(originalFileName).toPath();

            try {
                String contentType = Files.probeContentType(path);
                FileDto fileDto = dataBucketUtil.uploadFile(file, originalFileName, contentType);

                if (fileDto != null) {
                    inputFiles.add(new File(fileDto.getFileName(), fileDto.getFileUrl()));
                }
            } catch (Exception e) {
                //throw new GCPFileUploadException("Error occurred while uploading");
            }
        });
        //fileRepository.saveAll(inputFiles);
        return inputFiles;
    }
}
