using Domain.Common;
using Domain.Enums;

namespace Domain.Entities;

public class Item : EntityBase
{
    public string Name { get; set; }
    
    public string Description { get; set; }
    
    public Categories Category { get; set; }
}