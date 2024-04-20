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

public class BaseBrandBonusService : IBrandBonusService
{
    private readonly IBrandBonusesRepository _brandBonusRepository;
    private readonly IMapper _mapper;

    public BaseBrandBonusService(IBrandBonusesRepository brandBonusRepository, IMapper mapper)
    {
        _brandBonusRepository = brandBonusRepository;
        _mapper = mapper;
    }

    public async Task<BrandBonusDto> GetByIdAsync(string id, CancellationToken cancellationToken)
    {
        var brandBonus = await _brandBonusRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        return _mapper.Map<BrandBonusDto>(brandBonus);
    }

    public async Task<BrandBonusDto> CreateAsync(BrandBonusCreateDto dto, CancellationToken cancellationToken)
    {
        var brandBonus = _mapper.Map<BrandBonus>(dto);
        brandBonus.CreatedDateUtc = DateTime.UtcNow;

        await _brandBonusRepository.AddAsync(brandBonus, cancellationToken);

        return _mapper.Map<BrandBonusDto>(brandBonus);
    }

    public async Task<BrandBonusDto> UpdateAsync(string id, BrandBonusUpdateDto dto, CancellationToken cancellationToken)
    {
        var existingBrandBonus = await _brandBonusRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        if (existingBrandBonus == null)
        {
            return null;
        }

        _mapper.Map(dto, existingBrandBonus);
        await _brandBonusRepository.UpdateAsync(existingBrandBonus, cancellationToken);

        return _mapper.Map<BrandBonusDto>(existingBrandBonus);
    }

    public async Task<bool> DeleteAsync(string id, CancellationToken cancellationToken)
    {
        var existingBrandBonus = await _brandBonusRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        if (existingBrandBonus == null)
        {
            return false;
        }

        await _brandBonusRepository.DeleteAsync(existingBrandBonus, cancellationToken);
        return true;
    }

    public async Task<PagedList<BrandBonusDto>> GetWithPaginationAsync(int pageNumber, int pageSize, CancellationToken cancellationToken)
    {
        var brandBonuses = await _brandBonusRepository.GetPageAsync(pageNumber, pageSize, cancellationToken);
        var dtos = _mapper.Map<List<BrandBonusDto>>(brandBonuses);
        var totalCount = await _brandBonusRepository.GetTotalCountAsync();

        return new PagedList<BrandBonusDto>(dtos, pageNumber, pageSize, totalCount);
    }
}