using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;

namespace Application.IServices;

public interface IBrandsService
{
    Task<BrandDto> GetByIdAsync(string id, CancellationToken cancellationToken);

    Task<BrandDto> CreateAsync(BrandCreateDto dto, CancellationToken cancellationToken);

    Task<BrandDto> UpdateAsync(BrandUpdateDto dto, CancellationToken cancellationToken);

    Task<bool> DeleteAsync(string id, CancellationToken cancellationToken);

    Task<PagedList<BrandDto>> GetWithPaginationAsync(int pageNumber, int pageSize,
        CancellationToken cancellationToken);
}