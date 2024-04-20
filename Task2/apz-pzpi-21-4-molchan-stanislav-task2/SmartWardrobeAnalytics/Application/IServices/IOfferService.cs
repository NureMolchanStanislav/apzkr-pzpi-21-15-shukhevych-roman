using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;

namespace Application.IServices;

public interface IOfferService
{
    Task<OfferDto> GetByIdAsync(string id, CancellationToken cancellationToken);

    Task<OfferDto> CreateAsync(OfferCreateDto dto, CancellationToken cancellationToken);

    Task<OfferDto> UpdateAsync(string id, OfferUpdateDto dto, CancellationToken cancellationToken);

    Task<bool> DeleteAsync(string id, CancellationToken cancellationToken);

    Task<PagedList<OfferDto>> GetWithPaginationAsync(int pageNumber, int pageSize, CancellationToken cancellationToken);
}