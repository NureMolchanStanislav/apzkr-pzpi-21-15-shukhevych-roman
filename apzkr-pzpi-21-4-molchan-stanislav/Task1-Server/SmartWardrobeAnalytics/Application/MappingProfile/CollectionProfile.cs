using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class CollectionProfile : Profile
{
    public CollectionProfile()
    {
        CreateMap<Collection, CollectionDto>();
        CreateMap<CollectionDto, Collection>();
        CreateMap<Collection, CollectionUpdateDto>();
        CreateMap<CollectionUpdateDto, Collection>();
    }
}