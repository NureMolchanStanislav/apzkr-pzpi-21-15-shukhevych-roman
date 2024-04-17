using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class ItemProfile : Profile
{
    public ItemProfile()
    {
        CreateMap<Item, ItemDto>();
        CreateMap<ItemDto, Item>();
        CreateMap<ItemCreateDto, Item>();
        CreateMap<Item, ItemCreateDto>();
        CreateMap<ItemUpdateDto, Item>();
        CreateMap<Item, ItemUpdateDto>();
    }
}