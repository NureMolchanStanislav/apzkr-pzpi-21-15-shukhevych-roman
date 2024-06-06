using Domain.Common;
using MongoDB.Bson;

namespace Domain.Entities;

public class UsageHistory : EntityBase
{
    public ObjectId ItemId { get; set; }
        
    public string Event { get; set; }
}