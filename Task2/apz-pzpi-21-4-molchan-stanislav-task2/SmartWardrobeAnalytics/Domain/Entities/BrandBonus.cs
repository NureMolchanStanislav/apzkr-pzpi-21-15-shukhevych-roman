using Domain.Common;

namespace Domain.Entities;

public class BrandBonus : EntityBase
{
    public double Discount { get; set; }
    
    public int ConditionalNumberOfUsages { get; set; }
}