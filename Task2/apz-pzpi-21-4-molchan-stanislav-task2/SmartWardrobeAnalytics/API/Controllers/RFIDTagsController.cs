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

    [HttpPost("updateStatus")]
    public async Task<IActionResult> UpdateTagAndIncrementUsage([FromBody] RFIDTagStatusUpdate statusUpdate, CancellationToken cancellationToken)
    {
        if (statusUpdate == null || string.IsNullOrEmpty(statusUpdate.Id))
        {
            return BadRequest("Invalid RFID tag update data.");
        }

        bool updateResult = await _rfidTagsService.UpdateTagAndIncrementUsageAsync(statusUpdate, cancellationToken);

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
        var result = _rfidTagsService.CreateRFIDTagAsync(rfidTagCreateDto, cancellationToken);

        return Ok(result);
    }
}