package com.ebupt.portal.canyon.system.controller;

import com.ebupt.portal.canyon.common.annotation.EBLog;
import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.util.JsonResultUtil;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.entity.Log;
import com.ebupt.portal.canyon.system.service.LogService;
import com.ebupt.portal.canyon.system.vo.LogSearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 日志管理控制层
 *
 * @author chy
 * @date 2019-03-24 17:40
 */
@Setter
@Getter
@RestController
@RequestMapping("/log")
@Api(description = "日志管理")
public class LogController {

	private final LogService logService;

	@Autowired
	public LogController(LogService logService) {
		this.logService = logService;
	}

	@PostMapping
	@ApiOperation("分页查询日志记录")
	public JsonResult list(@Valid @RequestBody PageVo<LogSearchVo> pageVo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
			return JsonResult.paramError(errMsg);
		}

		Page<Log> page = this.logService.findByPage(pageVo);
		return JsonResultUtil.convert(page);
	}

	@DeleteMapping("/{daysAgo}")
	@ApiOperation("删除指定时间段之前的日志记录")
	@EBLog("删除{daysAgo}之前的日志")
	public JsonResult delete(@PathVariable("daysAgo") int daysAgo) {
		if (daysAgo < 30) {
			return JsonResult.paramError("30天内的日志不允许删除");
		}
		UpdateEnum result = this.logService.deleteByTime(daysAgo);
		return JsonResultUtil.convert(result);
	}
}
