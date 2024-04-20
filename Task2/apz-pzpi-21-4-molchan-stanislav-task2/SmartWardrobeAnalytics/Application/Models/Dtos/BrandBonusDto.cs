namespace Application.Models.Dtos;

public class BrandBonusDto
{
    public string Id { get; set; }
    
    public int ConditionalNumberOfUsages { get; set; }
    
    public int ConditionalDiscount { get; set; }
    
    public double ConditionalComplexity { get; set; }
    
    public double MaxDiscount { get; set; } 
    
    public string BrandId { get; set; }
}