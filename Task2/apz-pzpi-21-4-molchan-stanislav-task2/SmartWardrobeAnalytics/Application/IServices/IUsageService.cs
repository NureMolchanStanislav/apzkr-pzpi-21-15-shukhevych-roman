using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;

namespace Application.IServices;

public interface IUsageService
{
    Task<UsageDto> GetByIdAsync(string id, CancellationToken cancellationToken);
    Task<UsageDto> CreateAsync(UsagesCreateDto dto, CancellationToken cancellationToken);
    Task<UsageDto> UpdateAsync(UsagesUpdateDto dto, CancellationToken cancellationToken);
    Task<bool> DeleteAsync(string id, CancellationToken cancellationToken);
    Task<PagedList<UsageDto>> GetWithPaginationAsync(int pageNumber, int pageSize, CancellationToken cancellationToken);

    Task<int> CalculateTotalBrandUsageByUser(string brandId, CancellationToken cancellationToken);
}