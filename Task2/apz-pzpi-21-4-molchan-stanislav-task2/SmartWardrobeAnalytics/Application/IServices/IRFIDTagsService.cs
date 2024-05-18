using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;

namespace Application.IServices;

public interface IRFIDTagsService
{
    Task<bool> UpdateTagAndIncrementUsageAsync(string tagId, CancellationToken cancellationToken);
    
    Task<RFIDTagDto> CreateRFIDTagAsync(RFIDTagCreateDto createDto, CancellationToken cancellationToken);

    Task<bool> CheckForExistById(string id, CancellationToken cancellationToken);

    Task<List<RFIDGetDto>> GetAllByUser(CancellationToken cancellationToken);

    Task UpdateTag(string tagId, string itemId, CancellationToken cancellationToken);
}