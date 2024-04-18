using Domain.Common;
using MongoDB.Bson;

namespace Domain.Entities;

public class RFIDTag : EntityBase
{
    public bool Status { get; set; }
    
    public ObjectId ItemId { get; set; }
}