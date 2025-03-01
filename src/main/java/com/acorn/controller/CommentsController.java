package com.acorn.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acorn.dto.CommentsDto;
import com.acorn.process.CommentsProcess;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/main/eateries/")
@RequiredArgsConstructor
public class CommentsController {
	private final CommentsProcess process;
	
	/* -- Create: 댓글 작성 -- */
	@PostMapping("comments")
	public ResponseEntity<CommentsDto> createComment(@RequestBody CommentsDto dto) {
		return ResponseEntity.ok(process.createComment(dto));
	}
	
	/* -- Read: 특정 음식점의 댓글목록 읽기 -- */
	@GetMapping("{eateryNo}/comments")
	public ResponseEntity<Page<CommentsDto>> getCommentsByEatery(
		@PathVariable("eateryNo") int eateryNo, @RequestParam(name = "page", defaultValue = "0") int page) {
		Pageable pageable = PageRequest.of(page, 512); // 페이지당 항목 수 512개, 사실상 무한스크롤 취소
		return ResponseEntity.ok(process.getCommentsByEatery(eateryNo, pageable));
	}
	
	/* -- Update: 댓글 수정 -- */
	@PutMapping("comments/{no}")
	public ResponseEntity<CommentsDto> updateComment(@PathVariable("no") int no, @RequestBody CommentsDto dto){
		return ResponseEntity.ok(process.updateComment(no, dto));
	}
	
	/* -- Delete: 댓글 삭제 -- */
	@DeleteMapping("comments/{no}")
	public ResponseEntity<CommentsDto> deleteComment(@PathVariable("no") int no) {
		process.deleteComment(no);
		return ResponseEntity.noContent().build();
	}
}
