using Domain.Common;
using MongoDB.Bson;

namespace Domain.Entities;

public class Offer : EntityBase
{
    public double Discount { get; set; }
    
    public ObjectId BrandId { get; set; }
    
    public ObjectId UserId { get; set; }
}