using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;

namespace Application.IServices;

public interface IRFIDTagsService
{
    Task<bool> UpdateTagAndIncrementUsageAsync(RFIDTagStatusUpdate statusUpdate, CancellationToken cancellationToken);
    
    Task<RFIDTagDto> CreateRFIDTagAsync(RFIDTagCreateDto createDto, CancellationToken cancellationToken);
}