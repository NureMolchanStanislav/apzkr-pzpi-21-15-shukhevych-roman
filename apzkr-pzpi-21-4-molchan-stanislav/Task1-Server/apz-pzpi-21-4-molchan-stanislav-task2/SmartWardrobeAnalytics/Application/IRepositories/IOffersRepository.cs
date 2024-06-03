using Application.IRepositories.Base;
using Domain.Entities;

namespace Application.IRepositories;

public interface IOffersRepository : IBaseRepository<Offer>
{
    Task<Offer> UpdateAsync(Offer offer, CancellationToken cancellationToken);
}