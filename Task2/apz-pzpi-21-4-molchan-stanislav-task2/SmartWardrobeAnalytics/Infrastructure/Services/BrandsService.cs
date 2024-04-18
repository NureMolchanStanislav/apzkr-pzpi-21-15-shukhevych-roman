using Application.IRepositories;
using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;
using AutoMapper;
using Domain.Entities;
using MongoDB.Bson;

namespace Infrastructure.Services;

public class BrandsService(IBrandsRepository brandsRepository, IMapper mapper) : IBrandsService
{
    private readonly IBrandsRepository _brandsRepository = brandsRepository;
    private readonly IMapper _mapper = mapper;

    public async Task<BrandDto> GetByIdAsync(string id, CancellationToken cancellationToken)
    {
        var brand = await _brandsRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        var dto = _mapper.Map<BrandDto>(brand);
        return dto;
    }

    public async Task<BrandDto> CreateAsync(BrandCreateDto dto, CancellationToken cancellationToken)
    {
        var brand = _mapper.Map<Brand>(dto);
        brand.CreatedDateUtc = DateTime.UtcNow;

        await _brandsRepository.AddAsync(brand, cancellationToken);

        return _mapper.Map<BrandDto>(brand);
    }

    public async Task<BrandDto> UpdateAsync(BrandUpdateDto dto, CancellationToken cancellationToken)
    {
        var existingBrand = await _brandsRepository.GetOneAsync(ObjectId.Parse(dto.Id), cancellationToken);

        var updatedBrand = this._mapper.Map(dto, existingBrand);
        
        var result = await _brandsRepository.UpdateAsync(updatedBrand , cancellationToken);

        var itemDto = _mapper.Map<BrandDto>(result);
        
        return itemDto;
    }

    public async Task<bool> DeleteAsync(string id, CancellationToken cancellationToken)
    {
        var existingBrand = await _brandsRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);

        if (existingBrand == null)
        {
            return false;
        }

        await _brandsRepository.DeleteAsync(existingBrand, cancellationToken);
        return true;
    }

    public async Task<PagedList<BrandDto>> GetWithPaginationAsync(int pageNumber, int pageSize,
        CancellationToken cancellationToken)
    {
        var items = await _brandsRepository.GetPageAsync(pageNumber, pageSize, cancellationToken);
        var dtos = _mapper.Map<List<BrandDto>>(items);
        var totalCount = await _brandsRepository.GetTotalCountAsync();

        return new PagedList<BrandDto>(dtos, pageNumber, pageSize, totalCount);
    }
}