using Application.GlobalInstance;
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

public class OffersService : IOfferService
{
    private readonly IOffersRepository _offerRepository;
    private readonly IBrandsRepository _brandRepository;
    private readonly IMapper _mapper;

    public OffersService(IOffersRepository offerRepository, IMapper mapper, IBrandsRepository brandsRepository)
    {
        _offerRepository = offerRepository;
        _mapper = mapper;
        _brandRepository = brandsRepository;
    }

    public async Task<OfferDto> GetByIdAsync(string id, CancellationToken cancellationToken)
    {
        var offer = await _offerRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        return _mapper.Map<OfferDto>(offer);
    }
    
    public async Task<List<OfferInfo>> GetForUser(CancellationToken cancellationToken)
    {
        var offers = await _offerRepository.GetAllAsync(x => x.UserId == GlobalUser.Id, cancellationToken);
        var brandIds = offers.Select(o => o.BrandId).Distinct().ToList();
        var brands = await _brandRepository.GetAllAsync(b => brandIds.Contains(b.Id), cancellationToken);

        var dtos = offers.Select(offer => new OfferInfo()
        {
            Discount = offer.Discount,
            BrandName = brands.FirstOrDefault(b => b.Id == offer.BrandId)?.Name
        }).ToList();
    
        return dtos;
    }

    public async Task<OfferDto> CreateAsync(OfferCreateDto dto, CancellationToken cancellationToken)
    {
        var offer = _mapper.Map<Offer>(dto);
        offer.CreatedDateUtc = DateTime.UtcNow;

        await _offerRepository.AddAsync(offer, cancellationToken);

        return _mapper.Map<OfferDto>(offer);
    }

    public async Task<OfferDto> UpdateAsync(string id, OfferUpdateDto dto, CancellationToken cancellationToken)
    {
        var existingOffer = await _offerRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        if (existingOffer == null)
        {
            return null;
        }

        _mapper.Map(dto, existingOffer);
        await _offerRepository.UpdateAsync(existingOffer, cancellationToken);

        return _mapper.Map<OfferDto>(existingOffer);
    }

    public async Task<bool> DeleteAsync(string id, CancellationToken cancellationToken)
    {
        var existingOffer = await _offerRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        if (existingOffer == null)
        {
            return false;
        }

        await _offerRepository.DeleteAsync(existingOffer, cancellationToken);
        return true;
    }

    public async Task<PagedList<OfferDto>> GetWithPaginationAsync(int pageNumber, int pageSize, CancellationToken cancellationToken)
    {
        var offers = await _offerRepository.GetPageAsync(pageNumber, pageSize, cancellationToken);
        var dtos = _mapper.Map<List<OfferDto>>(offers);
        var totalCount = await _offerRepository.GetTotalCountAsync();

        return new PagedList<OfferDto>(dtos, pageNumber, pageSize, totalCount);
    }
}