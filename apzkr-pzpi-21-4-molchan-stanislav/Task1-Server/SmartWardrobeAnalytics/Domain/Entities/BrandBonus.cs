using Domain.Common;
using MongoDB.Bson;

namespace Domain.Entities;

public class BrandBonus : EntityBase
{
    public double ConditionalNumberOfUsages { get; set; }
    
    public double ConditionalDiscount { get; set; }
    
    public double ConditionalComplexity { get; set; }
    
    public double MaxDiscount { get; set; } 
    
    public ObjectId BrandId { get; set; }
}