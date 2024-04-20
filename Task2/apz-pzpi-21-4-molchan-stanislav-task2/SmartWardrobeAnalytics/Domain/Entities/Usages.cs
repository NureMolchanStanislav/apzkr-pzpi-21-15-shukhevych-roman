using Domain.Common;
using MongoDB.Bson;

namespace Domain.Entities;

public class Usages : EntityBase
{
    public string LastEvent { get; set; }
    
    public int TotalCount { get; set; }
    
    public ObjectId ItemId { get; set; }
}