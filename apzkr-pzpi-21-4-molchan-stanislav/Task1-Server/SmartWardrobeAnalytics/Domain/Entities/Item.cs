using Domain.Common;
using Domain.Enums;
using MongoDB.Bson;

namespace Domain.Entities;

public class Item : EntityBase
{
    public string Name { get; set; }
    
    public string Description { get; set; }
    
    public Categories Category { get; set; }
    
    public ObjectId BrandId { get; set; }
    
    public ObjectId CollectionId { get; set; }
}