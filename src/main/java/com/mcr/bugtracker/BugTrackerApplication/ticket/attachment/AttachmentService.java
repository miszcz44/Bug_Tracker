package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import com.mcr.bugtracker.BugTrackerApplication.GCS.DataBucketUtil;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketRepository;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.file.FileDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AttachmentService {

    private final TicketService ticketService;
    private final DataBucketUtil dataBucketUtil;

    private final AttachmentRepository attachmentRepository;
    private final AppUserRepository appUserRepository;
    private final TicketRepository ticketRepository;

    public Attachment saveAttachment(String fileName, String notes, String email, Long ticketId) {
        Attachment attachment = new Attachment(fileName, notes);
        attachment.setUploader(appUserRepository.findByEmail(email).get());
        attachment.setCreatedAt(LocalDateTime.now());
        attachment.setTicket(ticketRepository.findById(ticketId).get());
        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getAllAttachmentsByTicketId(Long ticketId) {
        return attachmentRepository.findAllAttachmentsByTicketId(ticketId);
    }

    public List<File> uploadFilesToGCS(MultipartFile[] files, Long ticketId) {
        List<File> inputFiles = new ArrayList<>();

        Arrays.asList(files).forEach(file -> {
            String originalFileName = file.getOriginalFilename();
            if(originalFileName == null){
                //throw new BadRequestException("Original file name is null");
            }
            Path path = new File(originalFileName).toPath();

            try {
                String contentType = Files.probeContentType(path);
                FileDto fileDto = dataBucketUtil.uploadFile(file, originalFileName, contentType, ticketId);

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
