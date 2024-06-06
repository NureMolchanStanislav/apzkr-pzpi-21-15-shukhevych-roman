using Domain.Common;

namespace Domain.Entities;

public class Collection : EntityBase
{
    public string Name { get; set; }
    
    public string Description { get; set; }
}