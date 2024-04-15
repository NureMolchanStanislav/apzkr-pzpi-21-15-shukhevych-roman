using Domain.Common;

namespace Domain.Entities;

public class Usages : EntityBase
{
    public string EventType { get; set; }
    
    public string LastValue { get; set; }
    
    public int Count { get; set; }
}