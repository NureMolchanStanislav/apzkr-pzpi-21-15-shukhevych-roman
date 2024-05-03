using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.UpdateDtos;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class RFIDTagsController : ControllerBase
{
    private readonly IRFIDTagsService _rfidTagsService;

    public RFIDTagsController(IRFIDTagsService rfidTagsService)
    {
        _rfidTagsService = rfidTagsService;
    }

    [HttpGet("{tagId}")]
    public async Task<bool> CheckForExistById(string tagId, CancellationToken cancellationToken)
    {
        return await _rfidTagsService.CheckForExistById(tagId, cancellationToken);
    }

    [HttpPut("updateStatus/{tagId}")]
    public async Task<IActionResult> UpdateTagAndIncrementUsage(string tagId, CancellationToken cancellationToken)
    {
        bool updateResult = await _rfidTagsService.UpdateTagAndIncrementUsageAsync(tagId, cancellationToken);

        if (updateResult)
        {
            return Ok("RFID tag status updated successfully.");
        }
        else
        {
            return BadRequest("Failed to update RFID tag status.");
        }
    }
    
    [HttpPost("create")]
    public async Task<IActionResult> CreateRFIDTag([FromBody] RFIDTagCreateDto rfidTagCreateDto, CancellationToken cancellationToken)
    {
        var result = await _rfidTagsService.CreateRFIDTagAsync(rfidTagCreateDto, cancellationToken);

        return Ok(result);
    }
}