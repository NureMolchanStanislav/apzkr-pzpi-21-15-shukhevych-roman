using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;

namespace Application.IServices;

public interface IBrandBonusService
{
    Task<BrandBonusDto> GetByIdAsync(string id, CancellationToken cancellationToken);

    Task<BrandBonusDto> CreateAsync(BrandBonusCreateDto dto, CancellationToken cancellationToken);

    Task<BrandBonusDto> UpdateAsync(BrandBonusUpdateDto dto, CancellationToken cancellationToken);

    Task<bool> DeleteAsync(string id, CancellationToken cancellationToken);

    Task<PagedList<BrandBonusDto>> GetWithPaginationAsync(int pageNumber, int pageSize,
        CancellationToken cancellationToken);

    Task<List<BrandBonusDto>> GetAllByUserAsync(CancellationToken cancellationToken);
}