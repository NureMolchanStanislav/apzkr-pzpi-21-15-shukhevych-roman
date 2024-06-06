using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class OfferProfile : Profile
{
    public OfferProfile()
    {
        CreateMap<Offer, OfferDto>();
        CreateMap<OfferDto, Offer>();
        CreateMap<OfferCreateDto, Offer>();
        CreateMap<Offer, OfferCreateDto>();
        CreateMap<OfferUpdateDto, Offer>();
        CreateMap<Offer, OfferUpdateDto>();
    }
}