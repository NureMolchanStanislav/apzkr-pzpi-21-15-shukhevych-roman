using Domain.Enums;

namespace Application.Models.Dtos;

public class ItemDto
{
    public string Name { get; set; }
    
    public string Description { get; set; }
    
    public Categories Categories { get; set; }
    
    public string BrandId { get; set; }
    
    public string CollectionId { get; set; }
}