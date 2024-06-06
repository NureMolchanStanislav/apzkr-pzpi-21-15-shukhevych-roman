using Domain.Enums;

namespace Application.Models.CreateDtos;

public class ItemCreateDto
{
    public string Name { get; set; }
    
    public string Description { get; set; }
    
    public Categories Categories { get; set; }
    
    public string BrandId { get; set; }
    
    public string CollectionId { get; set; }
}