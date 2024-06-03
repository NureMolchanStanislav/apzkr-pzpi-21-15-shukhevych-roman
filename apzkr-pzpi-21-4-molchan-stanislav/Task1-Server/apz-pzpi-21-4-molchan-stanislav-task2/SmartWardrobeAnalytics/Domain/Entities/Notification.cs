using Domain.Common;
using MongoDB.Bson;

namespace Domain.Entities;

public class Notification : EntityBase
{
    public int Condition { get; set; }
    
    public string Title { get; set; }
    
    public string Description { get; set; }
    
    public ObjectId ItemId { get; set; }
}