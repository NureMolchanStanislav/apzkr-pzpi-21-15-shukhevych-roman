using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
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

    [HttpGet()]
    public async Task<ActionResult<List<RFIDGetDto>>> GetAllByUser(CancellationToken cancellationToken)
    {
        return await _rfidTagsService.GetAllByUser(cancellationToken);
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
    
    [HttpPut("update/{tagId}/{itemId}")]
    public async Task<IActionResult> UpdateTag(string tagId, string itemId, CancellationToken cancellationToken)
    {
        await _rfidTagsService.UpdateTag(tagId, itemId, cancellationToken);
        return Ok();
    }
    
    [HttpPost("create")]
    public async Task<IActionResult> CreateRFIDTag([FromBody] RFIDTagCreateDto rfidTagCreateDto, CancellationToken cancellationToken)
    {
        var result = await _rfidTagsService.CreateRFIDTagAsync(rfidTagCreateDto, cancellationToken);

        return Ok(result);
    }
}